package org.imesense.dynamicspawncontrol.technical.config.gameworldtime;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.config.CfgClassAbstract;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotations.DCSSingleConfig;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 */
@DCSSingleConfig(fileName = "cfg_plugin_world_time")
public final class CfgPluginWorldTime extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgPluginWorldTime(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.FALSE);

		CodeGenericUtils.printInitClassToLog(this.getClass());

        DataPluginWorldTime.ConfigDataWorldTime.instance =
                new DataPluginWorldTime.ConfigDataWorldTime("game_world_time");

        if (Files.exists(Paths.get(this.nameConfig)))
        {
            this.loadFromFile();
        }
        else
        {
            this.saveToFile();
        }
    }

    /**
     *
     * @return
     */
    private static JsonObject getJsonObject()
    {
        JsonObject recordObject = new JsonObject();
        JsonObject jsonObjectWorldTime = new JsonObject();

        jsonObjectWorldTime.addProperty("day_length_minutes",
                DataPluginWorldTime.ConfigDataWorldTime.instance.getDayLengthMinutes());

        jsonObjectWorldTime.addProperty("night_length_minutes",
                DataPluginWorldTime.ConfigDataWorldTime.instance.getNightLengthMinutes());

        jsonObjectWorldTime.addProperty("sync_to_system_time_rate",
                DataPluginWorldTime.ConfigDataWorldTime.instance.getSyncToSystemTimeRate());

        jsonObjectWorldTime.addProperty("time_control_debug",
                DataPluginWorldTime.ConfigDataWorldTime.instance.getTimeControlDebug());

        jsonObjectWorldTime.addProperty("sync_to_system_time",
                DataPluginWorldTime.ConfigDataWorldTime.instance.getSyncToSystemTime());

        recordObject.add(DataPluginWorldTime.ConfigDataWorldTime.instance.
                getCategoryObject(), jsonObjectWorldTime);

        return recordObject;
    }

    /**
     *
     */
    @Override
    public void saveToFile()
    {
        Path configPath = Paths.get(this.nameConfig).getParent();

        if (Files.notExists(configPath))
        {
            try
            {
                Files.createDirectories(configPath);
            }
            catch (IOException exception)
            {
                throw new RuntimeException(exception);
            }
        }

        JsonObject recordObject = getJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter file = new FileWriter(this.nameConfig))
        {
            gson.toJson(recordObject, file);
        }
        catch (IOException exception)
        {
            throw new RuntimeException("Error writing to file: " + exception.getMessage(), exception);
        }
    }

    /**
     *
     */
    @Override
    public void loadFromFile()
    {
        try (FileReader fileReader = new FileReader(this.nameConfig))
        {
            JsonElement fileReaderJsonElement = new JsonParser().parse(fileReader);
            JsonObject readableObject = fileReaderJsonElement.getAsJsonObject();

            if (readableObject.has(DataPluginWorldTime.ConfigDataWorldTime.instance.getCategoryObject()))
            {
                JsonObject jsonObjectWorldTime =
                        readableObject.getAsJsonObject(DataPluginWorldTime.ConfigDataWorldTime.instance.getCategoryObject());

                if (jsonObjectWorldTime.has("day_length_minutes"))
                {
                    DataPluginWorldTime.ConfigDataWorldTime.instance.
                            setDayLengthMinutes(jsonObjectWorldTime.get("day_length_minutes").getAsInt());
                }

                if (jsonObjectWorldTime.has("night_length_minutes"))
                {
                    DataPluginWorldTime.ConfigDataWorldTime.instance.
                            setNightLengthMinutes(jsonObjectWorldTime.get("night_length_minutes").getAsInt());
                }

                if (jsonObjectWorldTime.has("sync_to_system_time_rate"))
                {
                    DataPluginWorldTime.ConfigDataWorldTime.instance.
                            setSyncToSystemTimeRate(jsonObjectWorldTime.get("sync_to_system_time_rate").getAsInt());
                }

                if (jsonObjectWorldTime.has("time_control_debug"))
                {
                    DataPluginWorldTime.ConfigDataWorldTime.instance.
                            setTimeControlDebug(jsonObjectWorldTime.get("time_control_debug").getAsBoolean());
                }

                if (jsonObjectWorldTime.has("sync_to_system_time"))
                {
                    DataPluginWorldTime.ConfigDataWorldTime.instance.
                            setSyncToSystemTime(jsonObjectWorldTime.get("sync_to_system_time").getAsBoolean());
                }
            }
            else
            {
                Log.writeDataToLogFile(2, "settings_block_nether_rack is missing in the config file.");
            }
        }
        catch (FileNotFoundException exception)
        {
            Log.writeDataToLogFile(2, "File not found: " + exception.getMessage());
        }
        catch (IOException exception)
        {
            Log.writeDataToLogFile(2, "IO Exception while loading: " + exception.getMessage());
        }
    }
}