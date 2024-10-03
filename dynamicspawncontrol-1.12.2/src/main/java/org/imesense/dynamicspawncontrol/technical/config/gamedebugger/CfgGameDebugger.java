package org.imesense.dynamicspawncontrol.technical.config.gamedebugger;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.config.CfgClassAbstract;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotations.DCSSingleConfig;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 *
 */
@DCSSingleConfig(fileName = "cfg_game_debugger.json")
public final class CfgGameDebugger extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgGameDebugger(String nameConfigFile)
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(this.getClass());

        DataGameDebugger.ConfigDataMonitor.instance = new DataGameDebugger.ConfigDataMonitor("monitor");
        DataGameDebugger.ConfigDataEvent.instance = new DataGameDebugger.ConfigDataEvent("event");

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

        JsonObject monitorObject = new JsonObject();
        monitorObject.addProperty("debug_monitor_cache", DataGameDebugger.ConfigDataMonitor.instance.getDebugMonitorCache());
        jsonObject.add(DataGameDebugger.ConfigDataEvent.instance.getCategoryObject(), monitorObject);

        JsonObject eventObject = new JsonObject();

        Map<String, Boolean> debugSettings = DataGameDebugger.ConfigDataEvent.instance.getDebugSettings();

        for (Map.Entry<String, Boolean> entry : debugSettings.entrySet())
        {
            eventObject.addProperty(entry.getKey(), entry.getValue());
        }

        jsonObject.add(DataGameDebugger.ConfigDataEvent.instance.getCategoryObject(), eventObject);

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
            catch (IOException exception)
            {
                throw new RuntimeException(exception);
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

            if (jsonObject.has(DataGameDebugger.ConfigDataMonitor.instance.getCategoryObject()))
            {
                JsonObject monitorObject = jsonObject.getAsJsonObject(DataGameDebugger.ConfigDataMonitor.instance.getCategoryObject());
                DataGameDebugger.ConfigDataMonitor.instance.setDebugMonitorCache(monitorObject.get("debug_monitor_cache").getAsBoolean());
            }

            if (jsonObject.has(DataGameDebugger.ConfigDataEvent.instance.getCategoryObject()))
            {
                JsonObject eventObject = jsonObject.getAsJsonObject(DataGameDebugger.ConfigDataEvent.instance.getCategoryObject());

                Map<String, Boolean> debugSettings = DataGameDebugger.ConfigDataEvent.instance.getDebugSettings();

                for (Map.Entry<String, Boolean> entry : debugSettings.entrySet())
                {
                    if (eventObject.has(entry.getKey()))
                    {
                        debugSettings.put(entry.getKey(), eventObject.get(entry.getKey()).getAsBoolean());
                    }
                }
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

