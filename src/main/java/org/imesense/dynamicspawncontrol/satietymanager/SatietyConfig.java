package org.imesense.dynamicspawncontrol.satietymanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SatietyConfig
{
    private static final String CONFIG_FILE_NAME = "food_satiety.json";
    private static Map<String, FoodSatietyData> satietyData = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

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
                LogManager.info("Created default satiety config with " + satietyData.size() + " food items");
            }
            else
            {
                LogManager.info("Loading existing satiety config...");
            }

            loadConfig(configFile);

            if (isDebugMode)
            {
                LogManager.info("Loaded satiety data for " + satietyData.size() + " items:");

                for (Map.Entry<String, FoodSatietyData> entry : satietyData.entrySet())
                {
                    FoodSatietyData data = entry.getValue();
                    String color = data.isPositive() ? "§a" : "§c";
                    LogManager.info("  " + entry.getKey() + ": " + color + data.getSatiety() + "§r satiety, " +
                            data.getSpanTime() + " seconds");
                }
            }
        }
        catch (IOException exception)
        {
            LogManager.error("Error creating/loading satiety config: " + exception.getMessage());
        }
    }

    private static void createDefaultConfig(File configFile) throws IOException
    {
        Map<String, FoodSatietyData> defaultData = new HashMap<>();

        // Minecraft:
        defaultData.put("minecraft:apple", new FoodSatietyData(25, 15));                // Яблоко
        defaultData.put("minecraft:mushroom_stew", new FoodSatietyData(100, 60));       // Грибной суп
        defaultData.put("minecraft:bread", new FoodSatietyData(25, 15));                // Хлеб
        defaultData.put("minecraft:porkchop", new FoodSatietyData(-1, 15));             // Сырая свинина
        defaultData.put("minecraft:cooked_porkchop", new FoodSatietyData(55, 35));      // Жареная свинина
        defaultData.put("minecraft:golden_apple", new FoodSatietyData(100, 85));        // Золотое яблоко
        defaultData.put("minecraft:fish", new FoodSatietyData(-1, 10));                 // Сырая рыба
        defaultData.put("minecraft:cooked_fish", new FoodSatietyData(40, 25));          // Жареная рыба
        defaultData.put("minecraft:cake", new FoodSatietyData(10, 1));                  // Торт
        defaultData.put("minecraft:cookie", new FoodSatietyData(10, 8));                // Печенье
        defaultData.put("minecraft:melon", new FoodSatietyData(15, 10));                // Арбуз
        defaultData.put("minecraft:beef", new FoodSatietyData(-1, 20));                 // Сырая говядина
        defaultData.put("minecraft:cooked_beef", new FoodSatietyData(50, 30));          // Стейк
        defaultData.put("minecraft:chicken", new FoodSatietyData(-1, 25));              // Сырая курица
        defaultData.put("minecraft:cooked_chicken", new FoodSatietyData(55, 35));       // Жареная курица
        defaultData.put("minecraft:rotten_flesh", new FoodSatietyData(-1, 35));         // Гнилая плоть
        defaultData.put("minecraft:spider_eye", new FoodSatietyData(-1, 40));           // Глаз паука
        defaultData.put("minecraft:carrot", new FoodSatietyData(25, 15));               // Морковь
        defaultData.put("minecraft:potato", new FoodSatietyData(-1, 20));               // Сырая картошка
        defaultData.put("minecraft:baked_potato", new FoodSatietyData(30, 20));         // Печеная картошка
        defaultData.put("minecraft:poisonous_potato", new FoodSatietyData(-1, 25));     // Ядовитая картошка
        defaultData.put("minecraft:pumpkin_pie", new FoodSatietyData(25, 15));          // Тыквенный пирог
        defaultData.put("minecraft:rabbit", new FoodSatietyData(-1, 12));               // Сырой кролик
        defaultData.put("minecraft:cooked_rabbit", new FoodSatietyData(50, 30));        // Жареный кролик
        defaultData.put("minecraft:rabbit_stew", new FoodSatietyData(100, 85));         // Рагу из кролика
        defaultData.put("minecraft:mutton", new FoodSatietyData(-1, 18));               // Сырая баранина
        defaultData.put("minecraft:cooked_mutton", new FoodSatietyData(65, 40));        // Жареная баранина
        defaultData.put("minecraft:beetroot", new FoodSatietyData(25, 15));             // Свекла
        defaultData.put("minecraft:beetroot_soup", new FoodSatietyData(75, 45));        // Свекольный суп
        defaultData.put("minecraft:golden_carrot", new FoodSatietyData(80, 50));        // Золотая морковь

        try (FileWriter writer = new FileWriter(configFile))
        {
            GSON.toJson(defaultData, writer);
        }

        satietyData = defaultData;
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
                LogManager.warn("Satiety config is empty");
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