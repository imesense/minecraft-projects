package org.imesense.dynamicspawncontrol.technical.parsers.beta;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.EnumSingleScripts;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.network.PacketTime;
import org.imesense.dynamicspawncontrol.technical.parsers.GeneralStorageData;
import org.imesense.dynamicspawncontrol.technical.parsers.IBetaParsers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 *
 */
public final class ParserSingleScriptCheckSpawn implements IBetaParsers
{
    /**
     *
     */
    private static ParserSingleScriptCheckSpawn instance;

    /**
     *
     */
    public ParserSingleScriptCheckSpawn()
    {
        CodeGenericUtils.printInitClassToLog(ParserSingleScriptCheckSpawn.class);
    }

    /**
     *
     * @return
     */
    public static ParserSingleScriptCheckSpawn getClassInstance()
    {
        return instance;
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
        instance = this;

        File file = getConfigFile(initialization, DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_SINGLE_SCRIPTS,
                EnumSingleScripts.SCRIPT_MOBS_LIST_SEE_SKY.getKeyword());

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
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        JsonObject emptyJson = new JsonObject();
                        JsonObject dataObject = new JsonObject();
                        dataObject.add("entities", new JsonArray());
                        emptyJson.add("data", dataObject);
                        gson.toJson(emptyJson, writer);
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

        try (FileReader reader = new FileReader(file))
        {
            Gson gson = new Gson();

            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonObject entitiesObject = jsonObject.getAsJsonObject("data");

            if (entitiesObject != null)
            {
                final Type listType = new TypeToken<List<String>>() {}.getType();
                GeneralStorageData.getInstance().EntitiesProhibitedOutdoors = gson.fromJson(entitiesObject.get("entities"), listType);

                Log.writeDataToLogFile(0, "Script: " +
                        EnumSingleScripts.SCRIPT_MOBS_LIST_SEE_SKY.getKeyword() + " data blockedEntities: " + GeneralStorageData.getInstance().EntitiesProhibitedOutdoors);
            }
            else
            {
                Log.writeDataToLogFile(0,
                        "Script: " +
                                EnumSingleScripts.SCRIPT_MOBS_LIST_SEE_SKY.getKeyword() +
                                " not found key 'entities'");

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
