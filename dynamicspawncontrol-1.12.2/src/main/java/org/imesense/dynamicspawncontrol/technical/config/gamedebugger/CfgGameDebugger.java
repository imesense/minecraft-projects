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

        DataGameDebugger.DebugMonitor.instance = new DataGameDebugger.DebugMonitor("monitor");
        DataGameDebugger.DebugEvent.instance = new DataGameDebugger.DebugEvent("event");

        if (Files.exists(Paths.get(this.nameConfig)))
        {
                loadFromFile();
        }
        else
        {
            Log.writeDataToLogFile(0, "Config file does not exist. Creating a new one.");
                saveToFile();
        }

        CodeGenericUtils.printInitClassToLog(CfgGameDebugger.class);
    }

    /**
     *
     * @return
     */
    private static JsonObject getJsonObject()
    {
        JsonObject eventObject = new JsonObject();

        eventObject.addProperty("debug_on_block_break", DataGameDebugger.DebugEvent.instance.getDebugOnBlockBreak());
        eventObject.addProperty("debug_on_block_place", DataGameDebugger.DebugEvent.instance.getDebugOnBlockPlace());
        eventObject.addProperty("debug_on_entity_spawn", DataGameDebugger.DebugEvent.instance.getDebugOnEntitySpawn());
        eventObject.addProperty("debug_on_left_click", DataGameDebugger.DebugEvent.instance.getDebugOnLeftClick());
        eventObject.addProperty("debug_on_living_drops", DataGameDebugger.DebugEvent.instance.getDebugOnLivingDrops());
        eventObject.addProperty("debug_on_living_experience_drop", DataGameDebugger.DebugEvent.instance.getDebugOnLivingExperienceDrop());
        eventObject.addProperty("debug_on_task_manager", DataGameDebugger.DebugEvent.instance.getDebugOnTaskManager());
        eventObject.addProperty("debug_on_player_tick", DataGameDebugger.DebugEvent.instance.getDebugOnPlayerTick());
        eventObject.addProperty("debug_on_potential_spawn", DataGameDebugger.DebugEvent.instance.getDebugOnPotentialSpawn());
        eventObject.addProperty("debug_on_right_click", DataGameDebugger.DebugEvent.instance.getDebugOnRightClick());

        return eventObject;
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

        JsonObject jsonObject = new JsonObject();
        JsonObject monitorObject = new JsonObject();

        monitorObject.addProperty("debug_monitor_cache", DataGameDebugger.DebugMonitor.instance.getDebugMonitorCache());
        jsonObject.add("monitor", monitorObject);

        JsonObject eventObject = getJsonObject();

        jsonObject.add("event", eventObject);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter file = new FileWriter(this.nameConfig))
        {
            gson.toJson(jsonObject, file);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error writing to file: " + e.getMessage(), e);
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

            if (jsonObject.has("debug_monitor_cache"))
            {
                JsonObject monitorObject = jsonObject.getAsJsonObject("debug_monitor_cache");
                DataGameDebugger.DebugMonitor.instance.setDebugMonitorCache(monitorObject.get("monitorDebug").getAsBoolean());
            }

            if (jsonObject.has("event"))
            {
                JsonObject eventObject = jsonObject.getAsJsonObject("event");
                DataGameDebugger.DebugEvent.instance.setDebugOnBlockBreak(eventObject.get("debug_on_block_break").getAsBoolean());
                DataGameDebugger.DebugEvent.instance.setDebugOnBlockPlace(eventObject.get("debug_on_block_place").getAsBoolean());
                DataGameDebugger.DebugEvent.instance.setDebugOnEntitySpawn(eventObject.get("debug_on_entity_spawn").getAsBoolean());
                DataGameDebugger.DebugEvent.instance.setDebugOnLeftClick(eventObject.get("debug_on_left_click").getAsBoolean());
                DataGameDebugger.DebugEvent.instance.setDebugOnLivingDrops(eventObject.get("debug_on_living_drops").getAsBoolean());
                DataGameDebugger.DebugEvent.instance.setDebugOnLivingExperienceDrop(eventObject.get("debug_on_living_experience_drop").getAsBoolean());
                DataGameDebugger.DebugEvent.instance.setDebugOnTaskManager(eventObject.get("debug_on_task_manager").getAsBoolean());
                DataGameDebugger.DebugEvent.instance.setDebugOnPlayerTick(eventObject.get("debug_on_player_tick").getAsBoolean());
                DataGameDebugger.DebugEvent.instance.setDebugOnPotentialSpawn(eventObject.get("debug_on_potential_spawn").getAsBoolean());
                DataGameDebugger.DebugEvent.instance.setDebugOnRightClick(eventObject.get("debug_on_right_click").getAsBoolean());

            }
        }
        catch (FileNotFoundException e)
        {
            Log.writeDataToLogFile(2, "File not found: " + e.getMessage());
        }
        catch (IOException e)
        {
            Log.writeDataToLogFile(2, "IO Exception while loading: " + e.getMessage());
        }
    }
}

