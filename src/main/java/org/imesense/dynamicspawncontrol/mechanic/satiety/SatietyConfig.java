package org.imesense.dynamicspawncontrol.mechanic.satiety;

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
    private static Map<String, SatietyFoodData> satietyData = new HashMap<>();
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

                for (Map.Entry<String, SatietyFoodData> entry : satietyData.entrySet())
                {
                    SatietyFoodData data = entry.getValue();
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
        Map<String, SatietyFoodData> defaultData = new HashMap<>();

        // Minecraft:
        defaultData.put("minecraft:apple", new SatietyFoodData(25, 15));                // Яблоко
        defaultData.put("minecraft:mushroom_stew", new SatietyFoodData(100, 60));       // Грибной суп
        defaultData.put("minecraft:bread", new SatietyFoodData(25, 15));                // Хлеб
        defaultData.put("minecraft:porkchop", new SatietyFoodData(-1, 15));             // Сырая свинина
        defaultData.put("minecraft:cooked_porkchop", new SatietyFoodData(55, 35));      // Жареная свинина
        defaultData.put("minecraft:golden_apple", new SatietyFoodData(100, 85));        // Золотое яблоко
        defaultData.put("minecraft:fish", new SatietyFoodData(-1, 10));                 // Сырая рыба
        defaultData.put("minecraft:cooked_fish", new SatietyFoodData(40, 25));          // Жареная рыба
        defaultData.put("minecraft:cake", new SatietyFoodData(10, 1));                  // Торт
        defaultData.put("minecraft:cookie", new SatietyFoodData(10, 8));                // Печенье
        defaultData.put("minecraft:melon", new SatietyFoodData(15, 10));                // Арбуз
        defaultData.put("minecraft:beef", new SatietyFoodData(-1, 20));                 // Сырая говядина
        defaultData.put("minecraft:cooked_beef", new SatietyFoodData(50, 30));          // Стейк
        defaultData.put("minecraft:chicken", new SatietyFoodData(-1, 25));              // Сырая курица
        defaultData.put("minecraft:cooked_chicken", new SatietyFoodData(55, 35));       // Жареная курица
        defaultData.put("minecraft:rotten_flesh", new SatietyFoodData(-1, 35));         // Гнилая плоть
        defaultData.put("minecraft:spider_eye", new SatietyFoodData(-1, 40));           // Глаз паука
        defaultData.put("minecraft:carrot", new SatietyFoodData(25, 15));               // Морковь
        defaultData.put("minecraft:potato", new SatietyFoodData(-1, 20));               // Сырая картошка
        defaultData.put("minecraft:baked_potato", new SatietyFoodData(30, 20));         // Печеная картошка
        defaultData.put("minecraft:poisonous_potato", new SatietyFoodData(-1, 25));     // Ядовитая картошка
        defaultData.put("minecraft:pumpkin_pie", new SatietyFoodData(25, 15));          // Тыквенный пирог
        defaultData.put("minecraft:rabbit", new SatietyFoodData(-1, 12));               // Сырой кролик
        defaultData.put("minecraft:cooked_rabbit", new SatietyFoodData(50, 30));        // Жареный кролик
        defaultData.put("minecraft:rabbit_stew", new SatietyFoodData(100, 85));         // Рагу из кролика
        defaultData.put("minecraft:mutton", new SatietyFoodData(-1, 18));               // Сырая баранина
        defaultData.put("minecraft:cooked_mutton", new SatietyFoodData(65, 40));        // Жареная баранина
        defaultData.put("minecraft:beetroot", new SatietyFoodData(25, 15));             // Свекла
        defaultData.put("minecraft:beetroot_soup", new SatietyFoodData(75, 45));        // Свекольный суп
        defaultData.put("minecraft:golden_carrot", new SatietyFoodData(80, 50));        // Золотая морковь

        try (FileWriter writer = new FileWriter(configFile))
        {
            GSON.toJson(defaultData, writer);
        }

        satietyData = defaultData;
    }

    private static void loadConfig(File configFile) throws IOException
    {
        Type type = new TypeToken<HashMap<String, SatietyFoodData>>() {}.getType();

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
        Map<String, SatietyFoodData> validData = new HashMap<>();

        for (Map.Entry<String, SatietyFoodData> entry : satietyData.entrySet())
        {
            String key = entry.getKey();
            SatietyFoodData data = entry.getValue();

            if (data.getSpanTime() < 1)
            {
                data = new SatietyFoodData(data.getSatiety(), 1);
            }

            validData.put(key, data);
        }

        satietyData = validData;
    }

    public static SatietyFoodData getSatietyData(String itemId)
    {
        return satietyData.get(itemId);
    }

    public static Map<String, SatietyFoodData> getAllSatietyData()
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
