package org.imesense.dynamicspawncontrol.config.file;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.config.data.GameDebuggerData;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.api.AConfig;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 *
 */
@ConceptConfig(fileName = "cfg_game_debugger")
public final class GameDebuggerAConfig extends AConfig
{
    /**
     *
     * @param nameConfigFile
     */
    public GameDebuggerAConfig(String nameConfigFile)
    {
        super(nameConfigFile, Boolean.TRUE);

        CodeGeneric.printInitClassToLog(this.getClass());

        GameDebuggerData.ConfigDataMonitor.Instance =
                new GameDebuggerData.ConfigDataMonitor("monitor");

        GameDebuggerData.ConfigDataEvent.Instance =
                new GameDebuggerData.ConfigDataEvent("event");

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
                GameDebuggerData.ConfigDataMonitor.Instance.getDebugMonitorCache());

        recordObject.add(GameDebuggerData.ConfigDataEvent.Instance.
                getCategoryObject(), jsonObjectMonitor);

        Map<String, Boolean> mapDebugSettings =
                GameDebuggerData.ConfigDataEvent.Instance.getDebugSettings();

        for (Map.Entry<String, Boolean> entry : mapDebugSettings.entrySet())
        {
            jsonObjectEvent.addProperty(entry.getKey(), entry.getValue());
        }

        recordObject.add(GameDebuggerData.ConfigDataEvent.Instance.
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

            if (readableObject.has(GameDebuggerData.ConfigDataMonitor.Instance.getCategoryObject()))
            {
                JsonObject jsonObjectMonitor =
                        readableObject.getAsJsonObject(GameDebuggerData.ConfigDataMonitor.Instance.getCategoryObject());

                GameDebuggerData.ConfigDataMonitor.Instance.setDebugMonitorCache(jsonObjectMonitor.get("debug_monitor_cache").getAsBoolean());
            }

            if (readableObject.has(GameDebuggerData.ConfigDataEvent.Instance.getCategoryObject()))
            {
                JsonObject jsonObjectEvent =
                        readableObject.getAsJsonObject(GameDebuggerData.ConfigDataEvent.Instance.
                                getCategoryObject());

                Map<String, Boolean> debugSettings =
                        GameDebuggerData.ConfigDataEvent.Instance.getDebugSettings();

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

