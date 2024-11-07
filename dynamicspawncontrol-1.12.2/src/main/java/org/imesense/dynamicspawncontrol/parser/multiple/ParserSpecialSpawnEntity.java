package org.imesense.dynamicspawncontrol.parser.multiple;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.technical.parser.GeneralStorageData;
import org.imesense.dynamicspawncontrol.core.api.AParser;

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
public final class ParserSpecialSpawnEntity extends AParser
{
    /**
     *
     */
    public ParserSpecialSpawnEntity()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     */
    @Override
    public void reloadConfig()
    {
        this.loadConfig(false);
    }

    /**
     *
     * @param initialization
     */
    @Override
    public void loadConfig(boolean initialization)
    {
        GeneralStorageData.Instance.EquipmentConfigs = new ArrayList<>();

        File file = getConfigFile(initialization, DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_SINGLE_SCRIPTS,
                /*EnumSingleScript.SCRIPT_ZOMBIE_SUMMON_AID.getKeyword()*/ "action_zombie_summon_aid.json");

        if (!file.exists())
        {
            try
            {
                File file1 = file.getParentFile();

                if (!file1.exists() && !file1.mkdirs())
                {
                    Log.writeDataToLogFile(0, "Failed to create directories for script file: " + file1.getAbsolutePath());
                    throw new RuntimeException("Failed to create directories for script file: " + file1.getAbsolutePath());
                }

                if (file.createNewFile())
                {
                    Log.writeDataToLogFile(0, "Created new script file: " + file.getAbsolutePath());

                    try (FileWriter fileWriter = new FileWriter(file))
                    {
                        fileWriter.write("[]");
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

        try (FileReader fileReader = new FileReader(file))
        {
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(fileReader, JsonArray.class);

            if (jsonArray.size() == 0)
            {
                //Log.writeDataToLogFile(0, "Script: " +
               //         EnumSingleScript.SCRIPT_ZOMBIE_SUMMON_AID.getKeyword() + " data is empty.");

                return;
            }

            for (int i = 0; i < jsonArray.size(); i++)
            {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                JsonObject jsonObject1 = jsonObject.getAsJsonObject("data");

                if (jsonObject1 != null)
                {
                    GeneralStorageData.Equipment config = new GeneralStorageData.Equipment();
                    config.Priority = jsonObject1.has("priority") ? jsonObject1.get("priority").getAsInt() : 0;

                    JsonObject jsonObject2 = jsonObject1.getAsJsonObject("equipment");

                    if (jsonObject2 != null)
                    {
                        Type listType = new TypeToken<List<String>>() {}.getType();

                        config.HeldItems = gson.fromJson(jsonObject2.get("held_item"), listType);
                        config.Helmets = gson.fromJson(jsonObject2.get("armor_helmet"), listType);
                        config.ChestPlates = gson.fromJson(jsonObject2.get("armor_chest"), listType);
                        config.Leggings = gson.fromJson(jsonObject2.get("armor_legs"), listType);
                        config.Boots = gson.fromJson(jsonObject2.get("armor_boots"), listType);

                        GeneralStorageData.Instance.EquipmentConfigs.add(config);

                        //Log.writeDataToLogFile(0, "Script: " +
                        //        EnumSingleScript.SCRIPT_ZOMBIE_SUMMON_AID.getKeyword() + " data loaded.");
                    }
                    else
                    {
                        //Log.writeDataToLogFile(0,
                        //        "Script: " +
                        //                EnumSingleScript.SCRIPT_ZOMBIE_SUMMON_AID.getKeyword() +
                        //                " not found key 'equipment'");

                        throw new RuntimeException("Key 'equipment' not found in JSON file.");
                    }
                }
                else
                {
                    //Log.writeDataToLogFile(0,
                   //         "Script: " +
                    //                EnumSingleScript.SCRIPT_ZOMBIE_SUMMON_AID.getKeyword() +
                    //                " not found key 'data'");

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
