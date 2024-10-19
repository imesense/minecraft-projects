package org.imesense.dynamicspawncontrol.technical.parsers.beta;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.EnumSingleScripts;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.parsers.GeneralStorageData;
import org.imesense.dynamicspawncontrol.technical.parsers.IBetaParsers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class ParserSingleZombieSummonAID implements IBetaParsers
{
    /**
     *
     */
    public ParserSingleZombieSummonAID()
    {
        CodeGenericUtils.printInitClassToLog(this.getClass());
    }

    /**
     *
     */
    public void reloadConfig()
    {
        loadConfig(false);
    }

    /**
     *
     * @param initialization
     */
    public void loadConfig(boolean initialization)
    {
        GeneralStorageData.instance.EquipmentConfigs = new ArrayList<>();

        File file = getConfigFile(initialization, ProjectStructure.STRUCT_FILES_DIRS.NAME_DIR_SINGLE_SCRIPTS,
                EnumSingleScripts.SCRIPT_ZOMBIE_SUMMON_AID.getKeyword());

        if (!file.exists())
        {
            try
            {
                File parentDir = file.getParentFile();
                if (!parentDir.exists() && !parentDir.mkdirs())
                {
                    Log.writeDataToLogFile(0, "Failed to create directories for script file: " + parentDir.getAbsolutePath());
                    throw new RuntimeException("Failed to create directories for script file: " + parentDir.getAbsolutePath());
                }

                if (file.createNewFile())
                {
                    Log.writeDataToLogFile(0, "Created new script file: " + file.getAbsolutePath());

                    try (FileWriter writer = new FileWriter(file))
                    {
                        writer.write("[]");
                        Log.writeDataToLogFile(0, "Initialized new script file with empty JSON array: " + file.getAbsolutePath());
                    }
                }
                else
                {
                    Log.writeDataToLogFile(0, "Failed to create new script file: " + file.getAbsolutePath());
                    throw new RuntimeException("Failed to create new script file: " + file.getAbsolutePath());
                }
            }
            catch (IOException exception)
            {
                Log.writeDataToLogFile(0, "Error creating new script file: " + exception.getMessage());
                throw new RuntimeException("Error creating new script file", exception);
            }
        }

        try (FileReader reader = new FileReader(file))
        {
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

            if (jsonArray.size() == 0)
            {
                Log.writeDataToLogFile(0, "Script: " +
                        EnumSingleScripts.SCRIPT_ZOMBIE_SUMMON_AID.getKeyword() + " data is empty.");

                return;
            }

            for (int i = 0; i < jsonArray.size(); i++)
            {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                JsonObject dataObject = jsonObject.getAsJsonObject("data");

                if (dataObject != null)
                {
                    GeneralStorageData.Equipment config = new GeneralStorageData.Equipment();
                    config.Priority = dataObject.has("priority") ? dataObject.get("priority").getAsInt() : 0;

                    JsonObject equipmentObject = dataObject.getAsJsonObject("equipment");

                    if (equipmentObject != null)
                    {
                        Type listType = new TypeToken<List<String>>() {}.getType();
                        config.HeldItems = gson.fromJson(equipmentObject.get("held_item"), listType);
                        config.Helmets = gson.fromJson(equipmentObject.get("armor_helmet"), listType);
                        config.ChestPlates = gson.fromJson(equipmentObject.get("armor_chest"), listType);
                        config.Leggings = gson.fromJson(equipmentObject.get("armor_legs"), listType);
                        config.Boots = gson.fromJson(equipmentObject.get("armor_boots"), listType);

                        GeneralStorageData.instance.EquipmentConfigs.add(config);

                        Log.writeDataToLogFile(0, "Script: " +
                                EnumSingleScripts.SCRIPT_ZOMBIE_SUMMON_AID.getKeyword() + " data loaded.");
                    }
                    else
                    {
                        Log.writeDataToLogFile(0,
                                "Script: " +
                                        EnumSingleScripts.SCRIPT_ZOMBIE_SUMMON_AID.getKeyword() +
                                        " not found key 'equipment'");
                        throw new RuntimeException("Key 'equipment' not found in JSON file.");
                    }
                }
                else
                {
                    Log.writeDataToLogFile(0,
                            "Script: " +
                                    EnumSingleScripts.SCRIPT_ZOMBIE_SUMMON_AID.getKeyword() +
                                    " not found key 'data'");
                    throw new RuntimeException("Key 'data' not found in JSON file.");
                }
            }
        }
        catch (JsonSyntaxException exception)
        {
            Log.writeDataToLogFile(0, "JSON syntax error in configuration file: " + exception.getMessage());
            throw new RuntimeException("JSON syntax error in configuration file", exception);
        }
        catch (IOException exception)
        {
            Log.writeDataToLogFile(0, "Error loading script file: " + exception.getMessage());
            throw new RuntimeException("Error loading script file", exception);
        }
    }
}
