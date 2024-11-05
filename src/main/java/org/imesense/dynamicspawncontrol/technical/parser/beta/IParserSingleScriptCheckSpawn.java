package org.imesense.dynamicspawncontrol.technical.parser.beta;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.technical.parser.GeneralStorageData;
import org.imesense.dynamicspawncontrol.core.api.IParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 *
 */
public final class IParserSingleScriptCheckSpawn implements IParser
{
    /**
     *
     */
    public IParserSingleScriptCheckSpawn()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
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
        File file = getConfigFile(initialization, DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_SINGLE_SCRIPTS,
                /*EnumSingleScript.SCRIPT_MOBS_LIST_SEE_SKY.getKeyword()*/"action_mobs_list_see_sky.json");

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
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();

                        JsonObject jsonObject = new JsonObject();
                        JsonObject jsonObject1 = new JsonObject();

                        jsonObject1.add("entities", new JsonArray());
                        jsonObject.add("data", jsonObject1);
                        gson.toJson(jsonObject, fileWriter);
                        Log.writeDataToLogFile(0, "Initialized new script file with empty JSON object: " + file.getAbsolutePath());
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

            JsonObject jsonObject = gson.fromJson(fileReader, JsonObject.class);
            JsonObject jsonObject1 = jsonObject.getAsJsonObject("data");

            if (jsonObject1 != null)
            {
                final Type listType = new TypeToken<List<String>>() {}.getType();
                GeneralStorageData.Instance.EntitiesProhibitedOutdoors = gson.fromJson(jsonObject1.get("entities"), listType);

                //Log.writeDataToLogFile(0, "Script: " +
                //        EnumSingleScript.SCRIPT_MOBS_LIST_SEE_SKY.getKeyword() +
                //            " data blockedEntities: " + GeneralStorageData.Instance.EntitiesProhibitedOutdoors);
            }
            else
            {
                //Log.writeDataToLogFile(0,
                //        "Script: " +
                //                EnumSingleScript.SCRIPT_MOBS_LIST_SEE_SKY.getKeyword() +
                //                " not found key 'entities'");

                throw new RuntimeException("Key 'entities' not found in JSON file.");
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
