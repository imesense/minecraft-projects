package org.imesense.dynamicspawncontrol.core.mixinconfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public final class MixinConfig
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type CONFIG_TYPE = new TypeToken<Map<String, MixinModConfig>>(){}.getType();

    private static File configFile;
    private static Map<String, MixinModConfig> configs = new HashMap<>();

    private static final List<String> DEFAULT_PARASITES_BLACKLIST = Arrays.asList(
        "divinerpg:saguaro_worm",
        "divinerpg:crab",
        "divinerpg:cyclops",
        "divinerpg:ehu",
        "divinerpg:brown_grizzle",
        "divinerpg:white_grizzle",
        "divinerpg:husk",
        "divinerpg:jack_o_man",
        "divinerpg:king_crab",
        "divinerpg:kobblin",
        "divinerpg:liopleurodon",
        "divinerpg:livestock_merchant",
        "divinerpg:pumpkin_spider",
        "divinerpg:rainbour",
        "divinerpg:shark",
        "divinerpg:smelter",
        "divinerpg:snapper",
        "divinerpg:stone_golem",
        "divinerpg:whale"
    );

    public static class MixinModConfig
    {
        public boolean enabled = true;

        public List<String> MobAttackingBlackList = new ArrayList<>();
    }

    public static void init(File globalDirectory)
    {
        try
        {
            File mixinDir = new File(globalDirectory,
                    DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_MIXINS);

            if (!mixinDir.exists())
            {
                mixinDir.mkdirs();
            }

            configFile = new File(mixinDir, "srparasites_mixin.json");
            loadConfig();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private static void loadConfig()
    {
        try
        {
            if (configFile.exists())
            {
                try (FileReader reader = new FileReader(configFile))
                {
                    configs = GSON.fromJson(reader, CONFIG_TYPE);
                }
            }
            else
            {
                createDefaultConfig();
            }

            if (!configs.containsKey("srparasites"))
            {
                MixinModConfig parasitesConfig = new MixinModConfig();
                parasitesConfig.MobAttackingBlackList.addAll(DEFAULT_PARASITES_BLACKLIST);
                configs.put("srparasites", parasitesConfig);
                saveConfig();
            }

        }
        catch (IOException exception)
        {
            System.err.println("Failed to load mixin config: " + exception.getMessage());
            configs = new HashMap<>();
            createDefaultConfig();
        }
    }

    private static void createDefaultConfig()
    {
        MixinModConfig parasitesConfig = new MixinModConfig();
        parasitesConfig.MobAttackingBlackList.addAll(DEFAULT_PARASITES_BLACKLIST);

        configs.put("srparasites", parasitesConfig);
        saveConfig();
    }

    private static void saveConfig()
    {
        try (FileWriter writer = new FileWriter(configFile))
        {
            GSON.toJson(configs, writer);
        }
        catch (IOException exception)
        {
            System.err.println("Failed to save mixin config: " + exception.getMessage());
        }
    }

    public static List<String> getParasitesBlacklist()
    {
        if (configs.containsKey("srparasites"))
        {
            return Collections.unmodifiableList(configs.get("srparasites").MobAttackingBlackList);
        }

        return Collections.unmodifiableList(DEFAULT_PARASITES_BLACKLIST);
    }

    public static void addToParasitesBlacklist(String entityName)
    {
        if (!configs.containsKey("srparasites"))
        {
            configs.put("srparasites", new MixinModConfig());
        }

        List<String> blacklist = configs.get("srparasites").MobAttackingBlackList;

        if (!blacklist.contains(entityName))
        {
            blacklist.add(entityName);
            saveConfig();
        }
    }

    public static void removeFromParasitesBlacklist(String entityName)
    {
        if (configs.containsKey("srparasites"))
        {
            List<String> blacklist = configs.get("srparasites").MobAttackingBlackList;

            if (blacklist.remove(entityName))
            {
                saveConfig();
            }
        }
    }

    public static void updateParasitesBlacklist(List<String> newList)
    {
        if (!configs.containsKey("srparasites"))
        {
            configs.put("srparasites", new MixinModConfig());
        }

        configs.get("srparasites").MobAttackingBlackList = new ArrayList<>(newList);
        saveConfig();
    }

    public static boolean isParasitesMixinEnabled()
    {
        return configs.containsKey("srparasites") && configs.get("srparasites").enabled;
    }
}