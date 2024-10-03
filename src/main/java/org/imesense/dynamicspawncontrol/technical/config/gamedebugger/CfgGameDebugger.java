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

        DataGameDebugger.ConfigDataMonitor.instance =
                new DataGameDebugger.ConfigDataMonitor("monitor");

        DataGameDebugger.ConfigDataEvent.instance =
                new DataGameDebugger.ConfigDataEvent("event");

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
        JsonObject jsonObjectEvent = new JsonObject();
        JsonObject jsonObjectMonitor = new JsonObject();

        jsonObjectMonitor.addProperty("debug_monitor_cache",
                DataGameDebugger.ConfigDataMonitor.instance.getDebugMonitorCache());

        recordObject.add(DataGameDebugger.ConfigDataEvent.instance.
                getCategoryObject(), jsonObjectMonitor);

        Map<String, Boolean> mapDebugSettings =
                DataGameDebugger.ConfigDataEvent.instance.getDebugSettings();

        for (Map.Entry<String, Boolean> entry : mapDebugSettings.entrySet())
        {
            jsonObjectEvent.addProperty(entry.getKey(), entry.getValue());
        }

        recordObject.add(DataGameDebugger.ConfigDataEvent.instance.
                getCategoryObject(), jsonObjectEvent);

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

            if (readableObject.has(DataGameDebugger.ConfigDataMonitor.instance.getCategoryObject()))
            {
                JsonObject jsonObjectMonitor =
                        readableObject.getAsJsonObject(DataGameDebugger.ConfigDataMonitor.instance.getCategoryObject());

                DataGameDebugger.ConfigDataMonitor.instance.setDebugMonitorCache(jsonObjectMonitor.get("debug_monitor_cache").getAsBoolean());
            }

            if (readableObject.has(DataGameDebugger.ConfigDataEvent.instance.getCategoryObject()))
            {
                JsonObject jsonObjectEvent =
                        readableObject.getAsJsonObject(DataGameDebugger.ConfigDataEvent.instance.
                                getCategoryObject());

                Map<String, Boolean> debugSettings =
                        DataGameDebugger.ConfigDataEvent.instance.getDebugSettings();

                for (Map.Entry<String, Boolean> entry : debugSettings.entrySet())
                {
                    if (jsonObjectEvent.has(entry.getKey()))
                    {
                        debugSettings.put(entry.getKey(), jsonObjectEvent.get(entry.getKey()).getAsBoolean());
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

