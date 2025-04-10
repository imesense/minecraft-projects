package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.handler;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

/* loaded from: input.jar:cad97/spawnercraft/handler/ConfigHandler.class */
public class ConfigHandler {
    public static Configuration config;
    public static int spawnerDropSilkLevel;
    public static boolean spawnerCraftable;
    public static boolean dropsRequireFishing;
    public static boolean isListBlacklist;
    public static final ConfigHandler instance = new ConfigHandler();
    private static String[] DEFAULT_DISABLED_MOBS = new String[0];
    public static List mobEssenceToggleList = Arrays.asList(DEFAULT_DISABLED_MOBS);
    public static Map<String, String> eggMapping = new HashMap();

    //private ConfigHandler() {
    //}

    public static void init(File configFile) {
    //    if (config == null) {
    //        config = new Configuration(configFile);
    //    }
    //    loadConfig();
    //    config.save();
    //    Log.writeDataToLogFile(0, "ConfigHandler initialized.");
    }

   // @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
     //   if (event.getModID().equalsIgnoreCase(SpawnerCraft.MOD_ID)) {
     //       loadConfig();
     //   }
    }

    private static void loadConfig() {
        spawnerDropSilkLevel = 0; //config.get("general", "Silk Touch for Spawner Drop", 0, "Required silk touch level to drop Empty Spawners from normal Mob Spawners.\nSet higher than obtainable silk touch (vanilla: >1) to disable.").getInt(0);
        spawnerCraftable = false;//config.get("general", "Is Empty Spawner Craftable", false, "Is it possible to craft an Empty Monster Spawner from iron bars?").setRequiresMcRestart(true).getBoolean();
        dropsRequireFishing = true;//config.get("general", "Essence Drops Require Fishing", true, "Do Mob Essence drops require the use of a Mob Fishing Pole?").getBoolean();
        mobEssenceToggleList = Arrays.asList(DEFAULT_DISABLED_MOBS);;//Arrays.asList(config.get("general", "Toggle Essence Validity", DEFAULT_DISABLED_MOBS, "Mobs which should not have essence (blacklist mode)\nMobs which should have essence (whitelist mode)").getStringList());
        isListBlacklist = true;//config.get("general", "Essence Blacklist Mode", true, "Toggle Essence should operate in blacklist mode (true) or whitelist mode (false)").getBoolean();
        //.getCategory("Egg Mapping").forEach(key, value -> {
       //     eggMapping.put(key, value.getString());
        //});
        //if (config.hasChanged()) {
        //    config.save();
        //}
    }
}
