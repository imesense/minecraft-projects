package org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.config;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseConfig;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;

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
@ConceptConfig(fileName = "cfg_time_control_mod_forge_1_12_2")
public final class CfgTimeControl extends BaseConfig
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgTimeControl(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.FALSE);

		CodeGeneric.printInitClassToLog(this.getClass());

        DataTimeControl.ConfigDataWorldTime.Instance =
                new DataTimeControl.ConfigDataWorldTime("time_control_mod_forge_1_12_2");

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
                DataTimeControl.ConfigDataWorldTime.Instance.getDayLengthMinutes());

        jsonObjectWorldTime.addProperty("night_length_minutes",
                DataTimeControl.ConfigDataWorldTime.Instance.getNightLengthMinutes());

        jsonObjectWorldTime.addProperty("sync_to_system_time_rate",
                DataTimeControl.ConfigDataWorldTime.Instance.getSyncToSystemTimeRate());

        jsonObjectWorldTime.addProperty("time_control_debug",
                DataTimeControl.ConfigDataWorldTime.Instance.getTimeControlDebug());

        jsonObjectWorldTime.addProperty("sync_to_system_time",
                DataTimeControl.ConfigDataWorldTime.Instance.getSyncToSystemTime());

        recordObject.add(DataTimeControl.ConfigDataWorldTime.Instance.
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

            if (readableObject.has(DataTimeControl.ConfigDataWorldTime.Instance.getCategoryObject()))
            {
                JsonObject jsonObjectWorldTime =
                        readableObject.getAsJsonObject(DataTimeControl.ConfigDataWorldTime.Instance.getCategoryObject());

                if (jsonObjectWorldTime.has("day_length_minutes"))
                {
                    DataTimeControl.ConfigDataWorldTime.Instance.
                            setDayLengthMinutes(jsonObjectWorldTime.get("day_length_minutes").getAsInt());
                }

                if (jsonObjectWorldTime.has("night_length_minutes"))
                {
                    DataTimeControl.ConfigDataWorldTime.Instance.
                            setNightLengthMinutes(jsonObjectWorldTime.get("night_length_minutes").getAsInt());
                }

                if (jsonObjectWorldTime.has("sync_to_system_time_rate"))
                {
                    DataTimeControl.ConfigDataWorldTime.Instance.
                            setSyncToSystemTimeRate(jsonObjectWorldTime.get("sync_to_system_time_rate").getAsInt());
                }

                if (jsonObjectWorldTime.has("time_control_debug"))
                {
                    DataTimeControl.ConfigDataWorldTime.Instance.
                            setTimeControlDebug(jsonObjectWorldTime.get("time_control_debug").getAsBoolean());
                }

                if (jsonObjectWorldTime.has("sync_to_system_time"))
                {
                    DataTimeControl.ConfigDataWorldTime.Instance.
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