package org.imesense.dynamicspawncontrol.technical.config.gamedebugger;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.config.CfgClassAbstract;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.DCSSingleConfig;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 *
 */
@DCSSingleConfig(fileName = "cfg_game_debugger")
public final class CfgGameDebugger extends CfgClassAbstract
{
    /**
     *
     * @param nameConfigFile
     */
    public CfgGameDebugger(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.TRUE);

        CodeGenericUtil.printInitClassToLog(this.getClass());

        DataGameDebugger.ConfigDataMonitor.Instance =
                new DataGameDebugger.ConfigDataMonitor("monitor");

        DataGameDebugger.ConfigDataEvent.Instance =
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
                DataGameDebugger.ConfigDataMonitor.Instance.getDebugMonitorCache());

        recordObject.add(DataGameDebugger.ConfigDataEvent.Instance.
                getCategoryObject(), jsonObjectMonitor);

        Map<String, Boolean> mapDebugSettings =
                DataGameDebugger.ConfigDataEvent.Instance.getDebugSettings();

        for (Map.Entry<String, Boolean> entry : mapDebugSettings.entrySet())
        {
            jsonObjectEvent.addProperty(entry.getKey(), entry.getValue());
        }

        recordObject.add(DataGameDebugger.ConfigDataEvent.Instance.
                getCategoryObject(), jsonObjectEvent);

        return recordObject;
    }

    /**
     *
     */
    @Override
    public void saveToFile()
    {
        Path path = Paths.get(this.nameConfig).getParent();

        if (Files.notExists(path))
        {
            try
            {
                Files.createDirectories(path);
            }
            catch (IOException exception)
            {
                throw new RuntimeException(exception);
            }
        }

        JsonObject recordObject = getJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter fileWriter = new FileWriter(this.nameConfig))
        {
            gson.toJson(recordObject, fileWriter);
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

            if (readableObject.has(DataGameDebugger.ConfigDataMonitor.Instance.getCategoryObject()))
            {
                JsonObject jsonObjectMonitor =
                        readableObject.getAsJsonObject(DataGameDebugger.ConfigDataMonitor.Instance.getCategoryObject());

                DataGameDebugger.ConfigDataMonitor.Instance.setDebugMonitorCache(jsonObjectMonitor.get("debug_monitor_cache").getAsBoolean());
            }

            if (readableObject.has(DataGameDebugger.ConfigDataEvent.Instance.getCategoryObject()))
            {
                JsonObject jsonObjectEvent =
                        readableObject.getAsJsonObject(DataGameDebugger.ConfigDataEvent.Instance.
                                getCategoryObject());

                Map<String, Boolean> debugSettings =
                        DataGameDebugger.ConfigDataEvent.Instance.getDebugSettings();

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

