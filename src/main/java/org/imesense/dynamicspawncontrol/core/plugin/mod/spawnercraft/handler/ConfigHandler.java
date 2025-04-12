package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ConfigHandler
{
    public static int spawnerDropSilkLevel = 0;
    public static boolean spawnerCraftable = false;
    public static boolean dropsRequireFishing = true;
    public static boolean isListBlacklist = true;
    public static final ConfigHandler instance = new ConfigHandler();
    private static String[] DEFAULT_DISABLED_MOBS = new String[0];
    public static List mobEssenceToggleList = Arrays.asList(DEFAULT_DISABLED_MOBS);
    public static Map<String, String> eggMapping = new HashMap();
}
