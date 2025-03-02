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
    public void reloadConfig()
    {
        GeneralCheckSpawnStorage.getInstance().entityEquipmentList.clear();
        GeneralCheckSpawnStorage.getInstance().profilePriorityList.clear();
        GeneralCheckSpawnStorage.getInstance().gameWorldList.clear();
        GeneralCheckSpawnStorage.getInstance().entityDescriptionsList.clear();
        GeneralCheckSpawnStorage.getInstance().entityAttributesList.clear();

        SupportCheckSpawnStorage.getInstance().dataSupportList.clear();

        this.loadConfig(false);
    }

    @Override
    public void loadConfig(boolean init)
    {
        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

        //-' TODO исправить создание файла

        try (FileReader fileReader = new FileReader(file))
        {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(fileReader, JsonObject.class);

            if (!jsonObject.has("configs"))
            {
                throw new RuntimeException("Key 'configs' not found in JSON file.");
            }

            JsonObject templates = jsonObject.has("templates") ?
                    jsonObject.getAsJsonObject("templates") : new JsonObject();

            JsonArray configs = jsonObject.getAsJsonArray("configs");

            for (JsonElement jsonElement : configs)
            {
                JsonObject dataObject = jsonElement.getAsJsonObject().getAsJsonObject("data");

                if (dataObject != null)
                {
                    if (!dataObject.has("profile") || !dataObject.has("description"))
                    {
                        throw new CheckScript.MissingRequiredFieldException("Fields 'profile' and 'description' are required in the 'data' section.");
                    }

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

                    if (entityDescriptionData.profile.isEmpty() || entityDescriptionData.description.isEmpty())
                    {
                        throw new CheckScript.MissingRequiredFieldException("Fields 'profile' and 'description' must not be empty in the 'data' section.");
                    }

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
                        JsonObject equipmentObject = dataObject.getAsJsonObject("equipment");

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

                        entityEquipmentData.hasShield = dataObject.has("has_shield") && dataObject.get("has_shield").getAsBoolean();
                    }

                    if (dataObject.has("potion"))
                    {
                        JsonArray potionArray = dataObject.getAsJsonArray("potion");
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

                    GeneralCheckSpawnStorage.getInstance().entityEquipmentList.add(entityEquipmentData);
                    GeneralCheckSpawnStorage.getInstance().profilePriorityList.add(profilePriorityData);
                    GeneralCheckSpawnStorage.getInstance().gameWorldList.add(gameWorldData);
                    GeneralCheckSpawnStorage.getInstance().entityDescriptionsList.add(entityDescriptionData);
                    GeneralCheckSpawnStorage.getInstance().entityAttributesList.add(entityAttributesData);
                }
                else
                {
                    throw new RuntimeException("Key 'data' not found in JSON file.");
                }
            }

            if (jsonObject.has("data_support"))
            {
                JsonArray jsonArray = jsonObject.getAsJsonArray("data_support");

                for (JsonElement element : jsonArray)
                {
                    JsonObject dataSupportObject = element.getAsJsonObject();

                    SupportCheckSpawnStorage.DataSupport dataSupport = new SupportCheckSpawnStorage.DataSupport();

                    dataSupport.seeSky = dataSupportObject.has("see_sky") ? dataSupportObject.get("see_sky").getAsBoolean() : null;
                    dataSupport.entityType = dataSupportObject.get("entity_type").getAsString();

                    if (dataSupportObject.has("potion"))
                    {
                        JsonArray potionArray = dataSupportObject.getAsJsonArray("potion");
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

                    SupportCheckSpawnStorage.getInstance().dataSupportList.add(dataSupport);
                }
            }
        }
        catch (JsonSyntaxException | IOException exception)
        {
            Log.writeDataToLogFile(0, "Error loading script file: " + exception.getMessage());
            throw new RuntimeException("Error loading script file", exception);
        }
        catch (CheckScript.MissingRequiredFieldException exception)
        {
            Log.writeDataToLogFile(0, exception.getMessage());
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }
}