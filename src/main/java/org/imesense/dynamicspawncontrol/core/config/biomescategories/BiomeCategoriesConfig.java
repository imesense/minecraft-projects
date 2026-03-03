package org.imesense.dynamicspawncontrol.core.config.biomescategories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public final class BiomeCategoriesConfig
{
    private static final String CONFIG_FILE_NAME = "biomes_categories.json";
    private static final Path CONFIG_FILE_PATH;

    public static class BiomeCategory
    {
        private String name;
        private int[] skulls;
        private List<String> biomes;

        public BiomeCategory() { }

        public BiomeCategory(String name, int[] skulls, List<String> biomes)
        {
            this.name = name;
            this.skulls = skulls;
            this.biomes = biomes;
        }

        public String getName() { return name; }
        public int[] getSkulls() { return skulls; }
        public List<String> getBiomes() { return biomes; }
    }

    private static final Map<String, BiomeCategory> BIOME_CATEGORY_MAP = new HashMap<>();
    private static final BiomeCategory DEFAULT_CATEGORY;
    private static List<BiomeCategory> allCategories = new ArrayList<>();

    static
    {
        DEFAULT_CATEGORY = new BiomeCategory("Неизвестный биом", new int[]{0, 1, 0, 1}, new ArrayList<>());

        String configDir = DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIRECTORY +
                File.separator +
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_COMPLEXITY_BIOMES;

        CONFIG_FILE_PATH = Paths.get("config", configDir, CONFIG_FILE_NAME);

        initialize();
    }

    private static void initialize()
    {
        try
        {
            Files.createDirectories(CONFIG_FILE_PATH.getParent());

            if (!Files.exists(CONFIG_FILE_PATH))
            {
                createDefaultConfig();
                LogManager.info("[DynamicSpawnControl] Created default biomes categories config at: " + CONFIG_FILE_PATH);
            }

            loadFromFile();

        }
        catch (Exception exception)
        {
            LogManager.error("[DynamicSpawnControl] Error initializing biomes config: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private static void createDefaultConfig() throws IOException
    {
        allCategories = createDefaultCategories();
        saveToFile();
    }

    private static List<BiomeCategory> createDefaultCategories()
    {
        List<BiomeCategory> categories = new ArrayList<>();

        categories.add(new BiomeCategory("Равнины", new int[]{0, 1, 0, 1},
                Arrays.asList("Plains", "Sunflower Plains")));

        categories.add(new BiomeCategory("Лес", new int[]{2, 1, 0, 0},
                Arrays.asList("Forest", "ForestHills", "Flower Forest", "Birch Forest",
                        "Birch Forest Hills", "Birch Forest Hills M", "Birch Forest M",
                        "Roofed Forest", "Roofed Forest M")));

        categories.add(new BiomeCategory("Тайга", new int[]{2, 0, 0, 0},
                Arrays.asList("Taiga", "TaigaHills", "Taiga M", "Mega Taiga",
                        "Mega Taiga Hills", "Mega Spruce Taiga")));

        categories.add(new BiomeCategory("Холодная тайга", new int[]{4, 1, 0, 0},
                Arrays.asList("Cold Taiga", "Cold Taiga M", "Cold Taiga Hills")));

        categories.add(new BiomeCategory("Джунгли", new int[]{7, 0, 0, 0},
                Arrays.asList("Jungle", "JungleHills", "Jungle M", "JungleEdge")));

        categories.add(new BiomeCategory("Горы", new int[]{4, 0, 1, 0},
                Arrays.asList("Extreme Hills", "Extreme Hills+", "Extreme Hills M", "Extreme Hills+ M")));

        categories.add(new BiomeCategory("Пустыня", new int[]{4, 0, 0, 0},
                Arrays.asList("Desert", "Desert M", "DesertHills")));

        categories.add(new BiomeCategory("Ледяные равнины", new int[]{3, 0, 1, 0},
                Arrays.asList("Ice Plains", "Ice Plains Spikes", "Ice Mountains")));

        categories.add(new BiomeCategory("Болото", new int[]{4, 2, 0, 0},
                Arrays.asList("Swampland")));

        categories.add(new BiomeCategory("Саванна", new int[]{0, 3, 0, 0},
                Arrays.asList("Savanna", "Savanna M", "Savanna Plateau", "Savanna Plateau M")));

        categories.add(new BiomeCategory("Меса", new int[]{5, 1, 0, 0},
                Arrays.asList("Mesa", "Mesa (Bryce)", "Mesa Plateau", "Mesa Plateau F")));

        categories.add(new BiomeCategory("Океан", new int[]{4, 2, 0, 0},
                Arrays.asList("Ocean", "FrozenOcean")));

        categories.add(new BiomeCategory("Глубокий океан", new int[]{7, 0, 0, 0},
                Arrays.asList("Deep Ocean")));

        categories.add(new BiomeCategory("Река", new int[]{0, 1, 0, 0},
                Arrays.asList("River", "FrozenRiver")));

        categories.add(new BiomeCategory("Пляж", new int[]{0, 2, 0, 0},
                Arrays.asList("Beach", "Stone Beach", "Cold Beach")));

        categories.add(new BiomeCategory("Грибной остров", new int[]{5, 2, 0, 0},
                Arrays.asList("MushroomIsland", "MushroomIslandShore")));

        categories.add(new BiomeCategory("Ад", new int[]{7, 0, 0, 0},
                Arrays.asList("Hell")));

        categories.add(new BiomeCategory("Энд", new int[]{6, 2, 0, 0},
                Arrays.asList("Sky", "The End")));

        return categories;
    }

    private static void saveToFile() throws IOException
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Writer writer = Files.newBufferedWriter(CONFIG_FILE_PATH))
        {
            gson.toJson(allCategories, writer);
        }
    }

    private static void loadFromFile()
    {
        try (Reader reader = Files.newBufferedReader(CONFIG_FILE_PATH))
        {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<BiomeCategory>>(){}.getType();
            allCategories = gson.fromJson(reader, listType);

            if (allCategories == null)
            {
                allCategories = new ArrayList<>();
            }

            rebuildMapping();

            LogManager.info("[DynamicSpawnControl] Loaded " + allCategories.size() +
                    " biome categories from config file");
        }
        catch (Exception exception)
        {
            LogManager.error("[DynamicSpawnControl] Error loading biomes categories: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private static void rebuildMapping()
    {
        BIOME_CATEGORY_MAP.clear();

        for (BiomeCategory category : allCategories)
        {
            if (category.biomes != null)
            {
                for (String biomesName : category.biomes)
                {
                    BIOME_CATEGORY_MAP.put(biomesName, category);
                }
            }
        }
    }

    public static BiomeCategory getCategoryForBiome(String biomeName)
    {
        return BIOME_CATEGORY_MAP.getOrDefault(biomeName, DEFAULT_CATEGORY);
    }

    public static BiomeCategory getDefaultCategory()
    {
        return DEFAULT_CATEGORY;
    }

    public static List<BiomeCategory> getAllCategories()
    {
        return Collections.unmodifiableList(allCategories);
    }

    public static void reload()
    {
        LogManager.info("[DynamicSpawnControl] Reloading biomes categories config...");
        loadFromFile();
    }

    public static void resetToDefault() throws IOException
    {
        LogManager.info("[DynamicSpawnControl] Resetting biomes categories to default...");
        allCategories = createDefaultCategories();
        saveToFile();
        rebuildMapping();
    }
}
