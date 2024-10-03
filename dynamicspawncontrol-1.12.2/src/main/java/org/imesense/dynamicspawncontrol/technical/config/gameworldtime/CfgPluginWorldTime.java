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
@DCSSingleConfig(fileName = "cfg_plugin_world_time.json")
public final class CfgPluginWorldTime extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgPluginWorldTime(String nameConfigFile)
    {
        super(nameConfigFile);

		CodeGenericUtils.printInitClassToLog(this.getClass());

        DataPluginWorldTime.worldTime.instance = new DataPluginWorldTime.worldTime("game_world_time");

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
        JsonObject jsonObject = new JsonObject();

        JsonObject settingsNetherRack = new JsonObject();

        settingsNetherRack.addProperty("day_length_minutes", DataPluginWorldTime.worldTime.instance.getDayLengthMinutes());
        settingsNetherRack.addProperty("night_length_minutes", DataPluginWorldTime.worldTime.instance.getNightLengthMinutes());
        settingsNetherRack.addProperty("sync_to_system_time_rate", DataPluginWorldTime.worldTime.instance.getSyncToSystemTimeRate());
        settingsNetherRack.addProperty("time_control_debug", DataPluginWorldTime.worldTime.instance.getTimeControlDebug());
        settingsNetherRack.addProperty("sync_to_system_time", DataPluginWorldTime.worldTime.instance.getSyncToSystemTime());

        jsonObject.add(DataPluginWorldTime.worldTime.instance.getCategoryObject(), settingsNetherRack);

        return jsonObject;
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
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

        JsonObject jsonObject = getJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter file = new FileWriter(this.nameConfig))
        {
            gson.toJson(jsonObject, file);
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
        try (FileReader reader = new FileReader(this.nameConfig))
        {
            JsonElement jsonElement = new JsonParser().parse(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.has(DataPluginWorldTime.worldTime.instance.getCategoryObject()))
            {
                JsonObject gameWorldTime = jsonObject.getAsJsonObject(DataPluginWorldTime.worldTime.instance.getCategoryObject());

                if (gameWorldTime.has("day_length_minutes"))
                {
                    DataPluginWorldTime.worldTime.instance.setDayLengthMinutes(gameWorldTime.get("day_length_minutes").getAsInt());
                }

                if (gameWorldTime.has("night_length_minutes"))
                {
                    DataPluginWorldTime.worldTime.instance.setNightLengthMinutes(gameWorldTime.get("night_length_minutes").getAsInt());
                }

                if (gameWorldTime.has("sync_to_system_time_rate"))
                {
                    DataPluginWorldTime.worldTime.instance.setSyncToSystemTimeRate(gameWorldTime.get("sync_to_system_time_rate").getAsInt());
                }

                if (gameWorldTime.has("time_control_debug"))
                {
                    DataPluginWorldTime.worldTime.instance.setTimeControlDebug(gameWorldTime.get("time_control_debug").getAsBoolean());
                }

                if (gameWorldTime.has("sync_to_system_time"))
                {
                    DataPluginWorldTime.worldTime.instance.setSyncToSystemTime(gameWorldTime.get("sync_to_system_time").getAsBoolean());
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