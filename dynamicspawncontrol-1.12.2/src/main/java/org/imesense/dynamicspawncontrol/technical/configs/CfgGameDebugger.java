package org.imesense.dynamicspawncontrol.technical.configs;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class CfgGameDebugger extends CustomConceptConfig
{
    public static CfgGameDebugger instance;

    private final Map<String, Map<String, Boolean>> configParams = new HashMap<>();

    @Override
    public boolean getConfigValue(String key) {
        return false;
    }

    public CfgGameDebugger(String nameConfigFile) throws IOException
    {
        super(nameConfigFile);

        CodeGenericUtils.printInitClassToLog(CfgGameDebugger.class);

        this.init();

        this.loadConfigFromFile();
    }

    @Override
    public void init() throws IOException
    {
        addConfigParamIfAbsent("debug_monitor", "monitor_debug", false); // Debug info cache
        addConfigParamIfAbsent("debug_event", "generic_maps_debug_event_effects", false); // Debug EventEffects
        addConfigParamIfAbsent("debug_event", "generic_maps_debug_drop_all_items", false); // Debug DropAllItems
        addConfigParamIfAbsent("debug_event", "generic_maps_debug_zombie_summon_aid", false); // Debug ZombieSummonAid
        addConfigParamIfAbsent("debug_event", "generic_maps_debug_event_left_mouse_click", false); // Debug EventLeftMouseClick
        addConfigParamIfAbsent("debug_event", "generic_maps_debug_main_override_spawn", false); // Debug MainOverrideSpawn
        addConfigParamIfAbsent("debug_event", "generic_maps_debug_event_right_mouse_click", false); // Debug EventRightMouseClick
        addConfigParamIfAbsent("debug_event", "generic_maps_debug_event_block_break", false); // Debug EventBlockBreak
        addConfigParamIfAbsent("debug_event", "generic_maps_debug_event_block_place", false); // Debug EventBlockPlace
        addConfigParamIfAbsent("debug_event", "generic_maps_debug_spawn_conditions", false); // Debug SpawnConditions
        addConfigParamIfAbsent("debug_event", "generic_maps_debug_drop_all_experience", false); // Debug DropAllExperience

        saveConfig();
    }

    private void addConfigParamIfAbsent(String category, String key, boolean defaultValue) {
        Map<String, Boolean> categoryParams = configParams.computeIfAbsent(category, k -> new HashMap<>());

        categoryParams.putIfAbsent(key, defaultValue);
    }

    public boolean isMonitorDebugEnabled() {
        return getConfigValue("debug_monitor", "monitor_debug");
    }

    private void addConfigParam(String category, String key, boolean defaultValue) {
        configParams.computeIfAbsent(category, k -> new HashMap<>()).put(key, defaultValue);
    }

    @Override
    public void loadConfigFromFile() throws IOException {
        File configFile = new File(this.nameConfig);

        Log.writeDataToLogFile(1, "Param file: " + configFile);

        if (!configFile.exists()) {
            return;
        }

        FileReader reader = new FileReader(configFile);
        JsonElement jsonElement = new JsonParser().parse(reader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String jsonString = jsonElement.toString();
        String configFilePath = configFile.getAbsolutePath();

        Log.writeDataToLogFile(1, String.format("File: %s, JSON Element: %s, JSON Object: %s, Config File Path: %s",
                configFilePath, jsonString, jsonObject, configFilePath));

        for (Map.Entry<String, Map<String, Boolean>> categoryEntry : configParams.entrySet()) {
            String category = categoryEntry.getKey();
            JsonObject categoryObject = jsonObject.getAsJsonObject(category);

            Log.writeDataToLogFile(1, "Param 0: " + category + " " + categoryObject);

            if (categoryObject != null) {
                for (Map.Entry<String, Boolean> paramEntry : categoryEntry.getValue().entrySet()) {
                    String key = paramEntry.getKey();
                    JsonElement element = categoryObject.get(key);

                    Log.writeDataToLogFile(1, "Param 1: " + key + " " + element);

                    if (element != null && element.isJsonPrimitive()) {
                        configParams.get(category).put(key, element.getAsBoolean());

                        Log.writeDataToLogFile(1, "Param 2: " + configParams.get(category).put(key, element.getAsBoolean()));
                    }
                }
            }
        }
    }

    public boolean getConfigValue(String category, String key) {
        Map<String, Boolean> categoryParams = configParams.get(category);
        if (categoryParams != null) {
            return categoryParams.getOrDefault(key, false);
        }
        return false;
    }

    private void saveConfig() throws IOException {
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<String, Map<String, Boolean>> categoryEntry : configParams.entrySet()) {
            String category = categoryEntry.getKey();
            JsonObject categoryObject = new JsonObject();

            for (Map.Entry<String, Boolean> paramEntry : categoryEntry.getValue().entrySet()) {
                categoryObject.addProperty(paramEntry.getKey(), paramEntry.getValue());
            }

            jsonObject.add(category, categoryObject);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter fileWriter = new FileWriter(this.nameConfig)) {
            gson.toJson(jsonObject, fileWriter);
        }
    }
}
