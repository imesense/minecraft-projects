package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.script.actioncollector.Equipment;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.data.*;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.storage.GeneralCheckSpawnStorage;
import org.imesense.dynamicspawncontrol.core.script.storage.checkspawn.storage.SupportCheckSpawnStorage;
import org.imesense.dynamicspawncontrol.core.script.syntax.CheckScript;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.api.AbstractConceptParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.imesense.dynamicspawncontrol.core.script.AuxScript.Util.*;

public final class ParserEventCheckSpawn extends AbstractConceptParser
{
    public ParserEventCheckSpawn(final String NAME_FILE)
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        this.nameFile = NAME_FILE;
    }

    @Override
    public void loadConfig(boolean init)
    {
        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

        if (!file.exists())
        {
            Log.writeDataToLogFile(0, "Config file not found, creating new: " + file);
            createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(fileReader, JsonArray.class);

            for (JsonElement jsonElement : jsonArray)
            {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                processJsonObject(jsonObject);
            }
        }
        catch (JsonSyntaxException | IOException exception)
        {
            handleLoadError("Error loading script file: " + exception.getMessage(), exception);
        }
        catch (CheckScript.MissingRequiredFieldException exception)
        {
            handleLoadError(exception.getMessage(), exception);
        }
    }

    protected void processJsonObject(JsonObject jsonObject) throws CheckScript.MissingRequiredFieldException
    {
        JsonObject templates = jsonObject.has("templates") ? jsonObject.getAsJsonObject("templates") : new JsonObject();

        if (jsonObject.has("configs"))
        {
            processConfigs(jsonObject.getAsJsonArray("configs"), templates);
        }

        if (jsonObject.has("data_support"))
        {
            processDataSupport(jsonObject.getAsJsonArray("data_support"));
        }
    }

    protected void processConfigs(JsonArray configs, JsonObject templates) throws CheckScript.MissingRequiredFieldException
    {
        for (JsonElement configElement : configs)
        {
            JsonObject dataObject = configElement.getAsJsonObject().getAsJsonObject("data");

            if (dataObject == null)
            {
                throw new RuntimeException("Key 'data' not found in JSON file.");
            }

            validateRequiredFields(dataObject);
            processDataObject(dataObject, templates);
        }
    }

    protected void validateRequiredFields(JsonObject dataObject) throws CheckScript.MissingRequiredFieldException
    {
        if (!dataObject.has("profile") || !dataObject.has("description"))
        {
            throw new CheckScript.MissingRequiredFieldException("Fields 'profile' and 'description' are required in the 'data' section.");
        }

        if (dataObject.get("profile").getAsString().isEmpty() || dataObject.get("description").getAsString().isEmpty())
        {
            throw new CheckScript.MissingRequiredFieldException("Fields 'profile' and 'description' must not be empty in the 'data' section.");
        }
    }

    protected void processDataObject(JsonObject dataObject, JsonObject templates)
    {
        if (dataObject.has("potion"))
        {
            dataObject.add("potion", resolveTemplate(dataObject.get("potion"), templates));
        }

        if (dataObject.has("command_nbt"))
        {
            dataObject.add("command_nbt", resolveTemplate(dataObject.get("command_nbt"), templates));
        }

        EntityEquipment.Data entityEquipmentData = new EntityEquipment.Data();
        EntityDescription.Data entityDescriptionData = new EntityDescription.Data();
        ProfilePriority.Data profilePriorityData = new ProfilePriority.Data();
        GameWorld.Data gameWorldData = new GameWorld.Data();
        EntityAttributes.Data entityAttributesData = new EntityAttributes.Data();

        entityDescriptionData.profile = dataObject.get("profile").getAsString();
        entityDescriptionData.description = dataObject.get("description").getAsString();
        entityDescriptionData.entityType = dataObject.get("entity_type").getAsString();

        profilePriorityData.priority = dataObject.has("priority") ? dataObject.get("priority").getAsInt() : 0;
        entityDescriptionData.isArcher = dataObject.has("is_archer") && dataObject.get("is_archer").getAsBoolean();
        gameWorldData.seeSky = dataObject.has("see_sky") ? dataObject.get("see_sky").getAsBoolean() : null;
        entityAttributesData.commandNbt = dataObject.has("command_nbt") ? dataObject.get("command_nbt").toString() : null;
        gameWorldData.maxHeight = dataObject.has("max_height") ? dataObject.get("max_height").getAsInt() : null;
        gameWorldData.minHeight = dataObject.has("min_height") ? dataObject.get("min_height").getAsInt() : null;
        entityDescriptionData.name = dataObject.has("name") ? dataObject.get("name").getAsString() : null;

        if (dataObject.has("equipment"))
        {
            processEquipment(dataObject.getAsJsonObject("equipment"), entityEquipmentData);
        }

        if (dataObject.has("potion"))
        {
            processPotionEffects(dataObject.getAsJsonArray("potion"), entityAttributesData);
        }

        GeneralCheckSpawnStorage.getInstance().entityEquipmentList.add(entityEquipmentData);
        GeneralCheckSpawnStorage.getInstance().profilePriorityList.add(profilePriorityData);
        GeneralCheckSpawnStorage.getInstance().gameWorldList.add(gameWorldData);
        GeneralCheckSpawnStorage.getInstance().entityDescriptionsList.add(entityDescriptionData);
        GeneralCheckSpawnStorage.getInstance().entityAttributesList.add(entityAttributesData);
    }

    protected void processEquipment(JsonObject equipmentObject, EntityEquipment.Data entityEquipmentData)
    {
        entityEquipmentData.heldItem = equipmentObject.has("held_item")
                ? Equipment.getInstance().parseItemList(equipmentObject.get("held_item"))
                : null;

        entityEquipmentData.helmet = equipmentObject.has("armor_helmet")
                ? Equipment.getInstance().parseItemList(equipmentObject.get("armor_helmet"))
                : null;

        entityEquipmentData.chestPlate = equipmentObject.has("armor_chest")
                ? Equipment.getInstance().parseItemList(equipmentObject.get("armor_chest"))
                : null;

        entityEquipmentData.legging = equipmentObject.has("armor_legs")
                ? Equipment.getInstance().parseItemList(equipmentObject.get("armor_legs"))
                : null;

        entityEquipmentData.boots = equipmentObject.has("armor_boots")
                ? Equipment.getInstance().parseItemList(equipmentObject.get("armor_boots"))
                : null;

        entityEquipmentData.hasShield = equipmentObject.has("has_shield") && equipmentObject.get("has_shield").getAsBoolean();
    }

    protected void processPotionEffects(JsonArray potionArray, EntityAttributes.Data entityAttributesData)
    {
        entityAttributesData.potion = new ArrayList<>();

        for (JsonElement potionElement : potionArray)
        {
            String potionString = potionElement.getAsString();
            String[] split = potionString.split(",");

            if (split.length < 3 || split.length > 4)
            {
                Log.writeDataToLogFile(2, "Bad potion specifier '" + potionString + "'! Use <potion>,<duration>,<amplifier>[,<chance>]");
                continue;
            }

            ResourceLocation potionId = new ResourceLocation(split[0].trim());
            Potion potion = ForgeRegistries.POTIONS.getValue(potionId);

            if (potion == null)
            {
                Log.writeDataToLogFile(2, "Can't find potion '" + potionId + "'!");
                continue;
            }

            Integer duration = Integer.parseInt(split[1].trim());
            Integer amplifier = Integer.parseInt(split[2].trim());
            Double chance = (split.length == 4) ? Double.parseDouble(split[3].trim()) : 1.0;

            entityAttributesData.potion.add(new PotionEffect.Data(new net.minecraft.potion.PotionEffect(potion, duration, amplifier), chance));
        }
    }

    protected void processPotionEffectsForDataSupport(JsonArray potionArray, SupportCheckSpawnStorage.DataSupport dataSupport)
    {
        dataSupport.potion = new ArrayList<>();

        for (JsonElement potionElement : potionArray)
        {
            String potionString = potionElement.getAsString();
            String[] split = potionString.split(",");

            if (split.length < 3 || split.length > 4)
            {
                Log.writeDataToLogFile(2, "Bad potion specifier '" + potionString + "'! Use <potion>,<duration>,<amplifier>[,<chance>]");
                continue;
            }

            ResourceLocation potionId = new ResourceLocation(split[0].trim());
            Potion potion = ForgeRegistries.POTIONS.getValue(potionId);

            if (potion == null)
            {
                Log.writeDataToLogFile(2, "Can't find potion '" + potionId + "'!");
                continue;
            }

            Integer duration = Integer.parseInt(split[1].trim());
            Integer amplifier = Integer.parseInt(split[2].trim());

            Double chance = (split.length == 4) ? Double.parseDouble(split[3].trim()) : 1.0;

            dataSupport.potion.add(new PotionEffect.Data(new net.minecraft.potion.PotionEffect(potion, duration, amplifier), chance));
        }
    }

    protected void processDataSupport(JsonArray dataSupportArray)
    {
        for (JsonElement element : dataSupportArray)
        {
            JsonObject dataSupportObject = element.getAsJsonObject();
            SupportCheckSpawnStorage.DataSupport dataSupport = new SupportCheckSpawnStorage.DataSupport();

            dataSupport.seeSky = dataSupportObject.has("see_sky") ? dataSupportObject.get("see_sky").getAsBoolean() : null;
            dataSupport.entityType = dataSupportObject.get("entity_type").getAsString();

            if (dataSupportObject.has("potion"))
            {
                processPotionEffectsForDataSupport(dataSupportObject.getAsJsonArray("potion"), dataSupport);
            }

            SupportCheckSpawnStorage.getInstance().dataSupportList.add(dataSupport);
        }
    }

    protected void handleLoadError(String message, Exception exception)
    {
        Log.writeDataToLogFile(0, message);
        throw new RuntimeException(message, exception);
    }

    @Override
    public void eraseData()
    {
        GeneralCheckSpawnStorage.getInstance().entityEquipmentList.clear();
        GeneralCheckSpawnStorage.getInstance().profilePriorityList.clear();
        GeneralCheckSpawnStorage.getInstance().gameWorldList.clear();
        GeneralCheckSpawnStorage.getInstance().entityDescriptionsList.clear();
        GeneralCheckSpawnStorage.getInstance().entityAttributesList.clear();

        SupportCheckSpawnStorage.getInstance().dataSupportList.clear();
    }
}