package org.imesense.dynamicspawncontrol.technical.configs;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public abstract class CustomConceptConfig
{
    protected String nameConfig;

    protected String constructPathToDirectory()
    {
        return DynamicSpawnControl.getGlobalPathToConfigs().getPath() + File.separator +
                DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator +
                DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_CONFIGS + File.separator;
    }

    public abstract void init() throws IOException;

    public abstract void loadConfigFromFile() throws IOException;

    public abstract boolean getConfigValue(String key);

    public CustomConceptConfig(String nameConfigFile) throws IOException
    {
        this.nameConfig = this.constructPathToDirectory() + nameConfigFile;

        JsonObject jsonObject = new JsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter file = new FileWriter(this.nameConfig))
        {
            gson.toJson(jsonObject, file);
        }
    }

    public void setBoolean(String key, boolean value, String category) throws IOException
    {
        JsonObject config = loadConfig();
        JsonObject categoryObj = config.has(category) ? config.getAsJsonObject(category) : new JsonObject();
        categoryObj.addProperty(key, value);
        config.add(category, categoryObj);
        saveConfig(config);
    }

    public void setBooleanArray(String key, boolean[] values, String category) throws IOException {
        JsonObject config = loadConfig();
        JsonObject categoryObj = config.has(category) ? config.getAsJsonObject(category) : new JsonObject();
        JsonArray jsonArray = new JsonArray();

        for (boolean value : values) {
            jsonArray.add(value);
        }

        categoryObj.add(key, jsonArray);
        config.add(category, categoryObj);
        saveConfig(config);
    }

    public void setInt(String key, int value, String category) throws IOException {
        JsonObject config = loadConfig();
        JsonObject categoryObj = config.has(category) ? config.getAsJsonObject(category) : new JsonObject();
        categoryObj.addProperty(key, value);
        config.add(category, categoryObj);
        saveConfig(config);
    }

    public void setIntArray(String key, int[] values, String category) throws IOException {
        JsonObject config = loadConfig();
        JsonObject categoryObj = config.has(category) ? config.getAsJsonObject(category) : new JsonObject();
        JsonArray jsonArray = new JsonArray();

        for (int value : values) {
            jsonArray.add(value);
        }

        categoryObj.add(key, jsonArray);
        config.add(category, categoryObj);
        saveConfig(config);
    }

    public void setDouble(String key, double value, String category) throws IOException {
        JsonObject config = loadConfig();
        JsonObject categoryObj = config.has(category) ? config.getAsJsonObject(category) : new JsonObject();
        categoryObj.addProperty(key, value);
        config.add(category, categoryObj);
        saveConfig(config);
    }

    public void setDoubleArray(String key, double[] values, String category) throws IOException {
        JsonObject config = loadConfig();
        JsonObject categoryObj = config.has(category) ? config.getAsJsonObject(category) : new JsonObject();
        JsonArray jsonArray = new JsonArray();

        for (double value : values) {
            jsonArray.add(value);
        }

        categoryObj.add(key, jsonArray);
        config.add(category, categoryObj);
        saveConfig(config);
    }

    public void setFloat(String key, float value, String category) throws IOException {
        JsonObject config = loadConfig();
        JsonObject categoryObj = config.has(category) ? config.getAsJsonObject(category) : new JsonObject();
        categoryObj.addProperty(key, value);
        config.add(category, categoryObj);
        saveConfig(config);
    }

    public void setFloatArray(String key, float[] values, String category) throws IOException {
        JsonObject config = loadConfig();
        JsonObject categoryObj = config.has(category) ? config.getAsJsonObject(category) : new JsonObject();
        JsonArray jsonArray = new JsonArray();

        for (float value : values) {
            jsonArray.add(value);
        }

        categoryObj.add(key, jsonArray);
        config.add(category, categoryObj);
        saveConfig(config);
    }

    public void setString(String key, String value, String category) throws IOException {
        JsonObject config = loadConfig();
        JsonObject categoryObj = config.has(category) ? config.getAsJsonObject(category) : new JsonObject();
        categoryObj.addProperty(key, value);
        config.add(category, categoryObj);
        saveConfig(config);
    }

    public void setStringArray(String key, String[] values, String category) throws IOException {
        JsonObject config = loadConfig();
        JsonObject categoryObj = config.has(category) ? config.getAsJsonObject(category) : new JsonObject();
        JsonArray jsonArray = new JsonArray();

        for (String value : values) {
            jsonArray.add(value);
        }

        categoryObj.add(key, jsonArray);
        config.add(category, categoryObj);
        saveConfig(config);
    }

    public void setByte(String key, byte value, String category) throws IOException {
        JsonObject config = loadConfig();
        JsonObject categoryObj = config.has(category) ? config.getAsJsonObject(category) : new JsonObject();
        categoryObj.addProperty(key, value);
        config.add(category, categoryObj);
        saveConfig(config);
    }

    public void setByteArray(String key, byte[] values, String category) throws IOException {
        JsonObject config = loadConfig();
        JsonObject categoryObj = config.has(category) ? config.getAsJsonObject(category) : new JsonObject();
        JsonArray jsonArray = new JsonArray();

        for (byte value : values) {
            jsonArray.add(value);
        }

        categoryObj.add(key, jsonArray);
        config.add(category, categoryObj);
        saveConfig(config);
    }

    private JsonObject loadConfig() throws IOException {
        File configFile = new File(this.nameConfig);

        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(reader);

                if (jsonElement.isJsonObject()) {
                    return jsonElement.getAsJsonObject();
                }
            }
        }

        return new JsonObject();
    }

    private void saveConfig(JsonObject config) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter file = new FileWriter(this.nameConfig)) {
            gson.toJson(config, file);
        }
    }
}
