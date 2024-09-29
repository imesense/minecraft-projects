package org.imesense.dynamicspawncontrol.technical.configs;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.configs.cfgGameDebugger.DataCategories;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotations.DCSSingleConfig;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@DCSSingleConfig(fileName = "game_debugger_config.json")
public final class CfgGameDebugger extends CustomConceptConfig {

    public CfgGameDebugger(String nameConfigFile) {
        super(nameConfigFile);

        Log.writeDataToLogFile(0, "nameConfigFile: " + nameConfigFile);

        DataCategories.DebugMonitor.instance = new DataCategories.DebugMonitor("monitor");
        DataCategories.DebugEvent.instance = new DataCategories.DebugEvent("event");

        if (Files.exists(Paths.get(this.nameConfig))) {
            try {
                loadFromFile();
            } catch (FileNotFoundException e) {
                Log.writeDataToLogFile(2, "File not found while loading: " + e.getMessage());
            } catch (IOException e) {
                Log.writeDataToLogFile(2, "IO Exception while loading: " + e.getMessage());
            }
        } else {
            Log.writeDataToLogFile(0, "Config file does not exist. Creating a new one.");
            try {
                saveToFile();
            } catch (IOException e) {
                Log.writeDataToLogFile(2, "IO Exception while saving: " + e.getMessage());
            }
        }

        CodeGenericUtils.printInitClassToLog(CfgGameDebugger.class);
    }

    public void saveToFile() throws IOException {
        Path configPath = Paths.get(this.nameConfig).getParent();
        if (Files.notExists(configPath)) {
            Log.writeDataToLogFile(0, "Creating directory: " + configPath);
            Files.createDirectories(configPath);
        }

        JsonObject jsonObject = new JsonObject();
        JsonObject monitorObject = new JsonObject();

        monitorObject.addProperty("debug_monitor_cache", DataCategories.DebugMonitor.instance.getDebugMonitorCache());
        jsonObject.add("monitor", monitorObject);

        JsonObject eventObject = new JsonObject();

        eventObject.addProperty("debug_on_block_break", DataCategories.DebugEvent.instance.getDebugOnBlockBreak());
        eventObject.addProperty("debug_on_block_place", DataCategories.DebugEvent.instance.getDebugOnBlockPlace());
        eventObject.addProperty("debug_on_entity_spawn", DataCategories.DebugEvent.instance.getDebugOnEntitySpawn());
        eventObject.addProperty("debug_on_left_click", DataCategories.DebugEvent.instance.getDebugOnLeftClick());
        eventObject.addProperty("debug_on_living_drops", DataCategories.DebugEvent.instance.getDebugOnLivingDrops());
        eventObject.addProperty("debug_on_living_experience_drop", DataCategories.DebugEvent.instance.getDebugOnLivingExperienceDrop());
        eventObject.addProperty("debug_on_task_manager", DataCategories.DebugEvent.instance.getDebugOnTaskManager());
        eventObject.addProperty("debug_on_player_tick", DataCategories.DebugEvent.instance.getDebugOnPlayerTick());
        eventObject.addProperty("debug_on_potential_spawn", DataCategories.DebugEvent.instance.getDebugOnPotentialSpawn());
        eventObject.addProperty("debug_on_right_click", DataCategories.DebugEvent.instance.getDebugOnRightClick());

        jsonObject.add("event", eventObject);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter file = new FileWriter(this.nameConfig)) {
            gson.toJson(jsonObject, file);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file: " + e.getMessage(), e);
        }
    }

    public void loadFromFile() throws IOException {
        try (FileReader reader = new FileReader(this.nameConfig)) {
            JsonElement jsonElement = new JsonParser().parse(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.has("debug_monitor_cache")) {
                JsonObject monitorObject = jsonObject.getAsJsonObject("debug_monitor_cache");
                DataCategories.DebugMonitor.instance.setDebugMonitorCache(monitorObject.get("monitorDebug").getAsBoolean());
            }

            if (jsonObject.has("event")) {
                JsonObject eventObject = jsonObject.getAsJsonObject("event");
                DataCategories.DebugEvent.instance.setDebugOnBlockBreak(eventObject.get("debug_on_block_break").getAsBoolean());
                DataCategories.DebugEvent.instance.setDebugOnBlockPlace(eventObject.get("debug_on_block_place").getAsBoolean());
                DataCategories.DebugEvent.instance.setDebugOnEntitySpawn(eventObject.get("debug_on_entity_spawn").getAsBoolean());
                DataCategories.DebugEvent.instance.setDebugOnLeftClick(eventObject.get("debug_on_left_click").getAsBoolean());
                DataCategories.DebugEvent.instance.setDebugOnLivingDrops(eventObject.get("debug_on_living_drops").getAsBoolean());
                DataCategories.DebugEvent.instance.setDebugOnLivingExperienceDrop(eventObject.get("debug_on_living_experience_drop").getAsBoolean());
                DataCategories.DebugEvent.instance.setDebugOnTaskManager(eventObject.get("debug_on_task_manager").getAsBoolean());
                DataCategories.DebugEvent.instance.setDebugOnPlayerTick(eventObject.get("debug_on_player_tick").getAsBoolean());
                DataCategories.DebugEvent.instance.setDebugOnPotentialSpawn(eventObject.get("debug_on_potential_spawn").getAsBoolean());
                DataCategories.DebugEvent.instance.setDebugOnRightClick(eventObject.get("debug_on_right_click").getAsBoolean());

            }
        } catch (FileNotFoundException e) {
            Log.writeDataToLogFile(2, "File not found: " + e.getMessage());
            throw e;
        } catch (IOException e) {
            Log.writeDataToLogFile(2, "IO Exception while loading: " + e.getMessage());
            throw e;
        }
    }
}

