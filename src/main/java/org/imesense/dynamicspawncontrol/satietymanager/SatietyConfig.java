package org.imesense.dynamicspawncontrol.satietymanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SatietyConfig
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE_NAME = "food_satiety.json";
    private static Map<String, FoodSatietyData> satietyData = new HashMap<>();

    public static void createFile(final String PATH, boolean isDebugMode)
    {
        try
        {
            File configDir = new File(PATH, DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_SATIETY_MANAGER);
            configDir.mkdirs();

            File configFile = new File(configDir, CONFIG_FILE_NAME);

            if (!configFile.exists())
            {
                createDefaultConfig(configFile);

                if (isDebugMode)
                {
                    System.out.println("[SatietyConfig] Created default config file: " + configFile.getAbsolutePath());
                }
            }

            loadConfig(configFile);

        }
        catch (IOException e)
        {
            System.err.println("[SatietyConfig] Error creating/loading config: " + e.getMessage());
        }
    }

    private static void createDefaultConfig(File configFile) throws IOException
    {
        Map<String, FoodSatietyData> defaultData = new HashMap<>();

        defaultData.put("minecraft:bread", new FoodSatietyData(2, 5));
        defaultData.put("minecraft:cookie", new FoodSatietyData(1, 3));
        defaultData.put("minecraft:rotten_flesh", new FoodSatietyData(-1, 10));
        defaultData.put("minecraft:spider_eye", new FoodSatietyData(-2, 15));
        defaultData.put("minecraft:golden_apple", new FoodSatietyData(10, 30));
        defaultData.put("minecraft:cake", new FoodSatietyData(4, 8));

        try (FileWriter writer = new FileWriter(configFile))
        {
            GSON.toJson(defaultData, writer);
        }
    }

    private static void loadConfig(File configFile) throws IOException
    {
        Type type = new TypeToken<HashMap<String, FoodSatietyData>>() {}.getType();

        try (FileReader reader = new FileReader(configFile))
        {
            satietyData = GSON.fromJson(reader, type);

            if (satietyData == null)
            {
                satietyData = new HashMap<>();
            }

            validateData();
        }
    }

    private static void validateData()
    {
        Map<String, FoodSatietyData> validData = new HashMap<>();

        for (Map.Entry<String, FoodSatietyData> entry : satietyData.entrySet())
        {
            String key = entry.getKey();
            FoodSatietyData data = entry.getValue();

            if (data.getSpanTime() < 1)
            {
                data = new FoodSatietyData(data.getSatiety(), 1);
            }

            validData.put(key, data);
        }

        satietyData = validData;
    }

    public static FoodSatietyData getSatietyData(String itemId)
    {
        return satietyData.get(itemId);
    }

    public static Map<String, FoodSatietyData> getAllSatietyData()
    {
        return new HashMap<>(satietyData);
    }

    public static void reloadConfig(String configPath) throws IOException
    {
        File configDir = new File(configPath, DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_SATIETY_MANAGER);
        File configFile = new File(configDir, CONFIG_FILE_NAME);

        if (configFile.exists())
        {
            loadConfig(configFile);
        }
    }
}