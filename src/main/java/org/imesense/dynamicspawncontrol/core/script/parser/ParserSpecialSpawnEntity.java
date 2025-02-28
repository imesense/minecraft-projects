package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.script.actioncollector.Equipment;
import org.imesense.dynamicspawncontrol.core.script.storage.StoringScriptData;
import org.imesense.dynamicspawncontrol.core.script.syntax.CheckScript;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.api.AbstractConceptParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.imesense.dynamicspawncontrol.core.script.AuxScript.Util.*;

public final class ParserSpecialSpawnEntity extends AbstractConceptParser
{
    public ParserSpecialSpawnEntity(final String NAME_FILE)
    {
        CodeGeneric.printInitClassToLog(this.getClass());
        this.nameFile = NAME_FILE;
    }

    @Override
    public void reloadConfig()
    {
        this.loadConfig(false);
    }

    @Override
    public void loadConfig(boolean initialization)
    {
        StoringScriptData.Instance.EquipmentConfigs = new ArrayList<>();
        StoringScriptData.Instance.Potions = new ArrayList<>();

        File file = getConfigFile(initialization,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

        if (!file.exists())
        {
            return;
        }

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

            for (JsonElement element : configs)
            {
                JsonObject dataObject = element.getAsJsonObject().getAsJsonObject("data");

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

                    StoringScriptData.Equipment config = new StoringScriptData.Equipment();
                    config.profile = dataObject.get("profile").getAsString();
                    config.description = dataObject.get("description").getAsString();
                    config.entityType = dataObject.get("entity_type").getAsString();
                    config.Priority = dataObject.has("priority") ? dataObject.get("priority").getAsInt() : 0;
                    config.isArcher = dataObject.has("is_archer") && dataObject.get("is_archer").getAsBoolean();
                    config.seeSky = dataObject.has("see_sky") ? dataObject.get("see_sky").getAsBoolean() : null;
                    config.commandNbt = dataObject.has("command_nbt") ? dataObject.get("command_nbt").toString() : null;
                    config.maxHeight = dataObject.has("max_height") ? dataObject.get("max_height").getAsInt() : null;
                    config.minHeight = dataObject.has("min_height") ? dataObject.get("min_height").getAsInt() : null;

                    JsonObject equipmentObject = dataObject.getAsJsonObject("equipment");

                    if (equipmentObject != null)
                    {
                        config.HeldItems = Equipment.getInstance().parseItemList(equipmentObject.get("held_item"));
                        config.Helmets = Equipment.getInstance().parseItemList(equipmentObject.get("armor_helmet"));
                        config.ChestPlates = Equipment.getInstance().parseItemList(equipmentObject.get("armor_chest"));
                        config.Leggings = Equipment.getInstance().parseItemList(equipmentObject.get("armor_legs"));
                        config.Boots = Equipment.getInstance().parseItemList(equipmentObject.get("armor_boots"));
                        config.HasShield = dataObject.has("has_shield") && dataObject.get("has_shield").getAsBoolean();

                        StoringScriptData.Instance.EquipmentConfigs.add(config);
                    }
                    else
                    {
                        throw new RuntimeException("Key 'equipment' not found in JSON file.");
                    }

                    if (dataObject.has("potion"))
                    {
                        JsonArray potionArray = dataObject.getAsJsonArray("potion");

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

                            int duration = Integer.parseInt(split[1].trim());
                            int amplifier = Integer.parseInt(split[2].trim());
                            double chance = (split.length == 4) ? Double.parseDouble(split[3].trim()) : 1.0;

                            StoringScriptData.Instance.Potions.add(new StoringScriptData.PotionEffectWithChance(new PotionEffect(potion, duration, amplifier), chance));
                        }
                    }
                }
                else
                {
                    throw new RuntimeException("Key 'data' not found in JSON file.");
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