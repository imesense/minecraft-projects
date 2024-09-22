package org.imesense.dynamicspawncontrol.technical.parsers.beta;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
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
public class ParserSingleZombieSummonAID implements IBetaParsers
{
    /**
     *
     */
    private static ParserSingleZombieSummonAID _classInstance;

    /**
     *
     * @param nameClass
     */
    public ParserSingleZombieSummonAID(String nameClass)
    {
        Log.writeDataToLogFile(0, nameClass);
    }

    /**
     *
     * @return
     */
    public static ParserSingleZombieSummonAID getClassInstance()
    {
        return _classInstance;
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
        _classInstance = this;
        GeneralStorageData.getInstance()._equipmentConfigs = new ArrayList<>();

        File file = (initialization)
                ? new File(DynamicSpawnControl.getGlobalPathToConfigs().getPath() + File.separator +
                DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIRECTORY +
                File.separator + DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_SINGLE_SCRIPTS,
                EnumSingleScripts.SCRIPT_ZOMBIE_SUMMON_AID.getKeyword())
                : new File("config/DynamicSpawnControl/single_scripts/" +
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
            catch (IOException e)
            {
                Log.writeDataToLogFile(0, "Error creating new script file: " + e.getMessage());
                throw new RuntimeException("Error creating new script file", e);
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
                    GeneralStorageData.EquipmentConfig config = new GeneralStorageData.EquipmentConfig();
                    config._priority = dataObject.has("priority") ? dataObject.get("priority").getAsInt() : 0;

                    JsonObject equipmentObject = dataObject.getAsJsonObject("equipment");

                    if (equipmentObject != null)
                    {
                        Type listType = new TypeToken<List<String>>() {}.getType();
                        config._heldItems = gson.fromJson(equipmentObject.get("held_item"), listType);
                        config._helmets = gson.fromJson(equipmentObject.get("armor_helmet"), listType);
                        config._chestPlates = gson.fromJson(equipmentObject.get("armor_chest"), listType);
                        config._leggings = gson.fromJson(equipmentObject.get("armor_legs"), listType);
                        config._boots = gson.fromJson(equipmentObject.get("armor_boots"), listType);

                        GeneralStorageData.getInstance()._equipmentConfigs.add(config);

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
