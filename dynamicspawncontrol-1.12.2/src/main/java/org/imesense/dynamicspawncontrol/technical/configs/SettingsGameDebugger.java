package org.imesense.dynamicspawncontrol.technical.configs;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.proxy.ClientProxy;

import java.io.File;

/**
 *
 */
public class SettingsGameDebugger implements IConfig
{
    /**
     *
     */
    public static boolean DebugMonitorCache = false;

    /**
     *
     */
    public static boolean DebugGenericPlayerTick = false;

    /**
     *
     */
    public static boolean DebugGenericLivingDrops = false;

    /**
     *
     */
    public static boolean DebugGenericSummonAidEvent = false;

    /**
     *
     */
    public static boolean DebugGenericLeftClickEvent = false;

    /**
     *
     */
    public static boolean DebugGenericPotentialSpawns = false;

    /**
     *
     */
    public static boolean DebugGenericRightClickEvent = false;

    /**
     *
     */
    public static boolean DebugGenericBlockBreakEvent = false;

    /**
     *
     */
    public static boolean DebugGenericBlockPlaceEvent = false;

    /**
     *
     */
    public static boolean DebugGenericEntitySpawnEvent = false;

    /**
     *
     */
    public static boolean DebugGenericLivingExperienceDrop = false;

    /**
     *
     * @param nameClass
     */
    public SettingsGameDebugger(final String nameClass)
    {

    }

    /**
     *
     * @param event
     * @param nameClass
     */
    @Override
    public void init(FMLPreInitializationEvent event, final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);

        ClientProxy.ConfigGameDebugger = new Configuration(new File(DynamicSpawnControl.getGlobalPathToConfigs().getPath() +
                File.separator + DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIRECTORY +
                File.separator + DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_CONFIGS,
                "game_debugger" + DynamicSpawnControl.STRUCT_FILES_EXTENSION.CONFIG_FILE_EXTENSION));

        this.read();
    }

    /**
     *
     * @param configuration
     */
    @Override
    public void readProperties(Configuration configuration)
    {
        DebugMonitorCache =
                configuration.getBoolean
                        ("Debug info cache", "monitor_debug", DebugMonitorCache,
                                "Translates the cache parameters to the screen in static text");

        DebugGenericPlayerTick =
                configuration.getBoolean
                        ("Debug EventEffects", "generic_maps_debug", DebugGenericPlayerTick,
                                "Debugging the 'EventEffects.json'");

        DebugGenericLivingDrops =
                configuration.getBoolean
                        ("Debug DropAllItems", "generic_maps_debug", DebugGenericLivingDrops,
                                "Debugging the 'DropAllItems.json'");

        DebugGenericSummonAidEvent =
                configuration.getBoolean
                        ("Debug ZombieSummonAid", "generic_maps_debug", DebugGenericSummonAidEvent,
                                "Debugging the 'ZombieSummonAid.json'");

        DebugGenericLeftClickEvent =
                configuration.getBoolean
                        ("Debug EventLeftMouseClick", "generic_maps_debug", DebugGenericLeftClickEvent,
                                "Debugging the 'EventLeftMouseClick.json'");

        DebugGenericPotentialSpawns =
                configuration.getBoolean
                        ("Debug MainOverrideSpawn", "generic_maps_debug", DebugGenericPotentialSpawns,
                                "Debugging the 'MainOverrideSpawn.json'");

        DebugGenericRightClickEvent =
                configuration.getBoolean
                        ("Debug EventRightMouseClick", "generic_maps_debug", DebugGenericRightClickEvent,
                                "Debugging the 'EventRightMouseClick.json'");

        DebugGenericBlockBreakEvent =
                configuration.getBoolean
                        ("Debug EventBlockBreak", "generic_maps_debug", DebugGenericBlockBreakEvent,
                                "Debugging the 'EventBlockBreak.json'");

        DebugGenericBlockPlaceEvent =
                configuration.getBoolean
                        ("Debug EventBlockPlace", "generic_maps_debug", DebugGenericBlockPlaceEvent,
                                "Debugging the 'EventBlockPlace.json'");

        DebugGenericEntitySpawnEvent =
                configuration.getBoolean
                        ("Debug SpawnConditions", "generic_maps_debug", DebugGenericEntitySpawnEvent,
                                "Debugging the 'SpawnConditions.json'");

        DebugGenericLivingExperienceDrop =
                configuration.getBoolean
                        ("Debug DropAllExperience", "generic_maps_debug", DebugGenericLivingExperienceDrop,
                                "Debugging the 'DropAllExperience.json'");
    }

    /**
     *
     */
    @Override
    public void read()
    {
        Configuration configuration = ClientProxy.ConfigGameDebugger;

        try
        {
            configuration.load();

            this.readProperties(configuration);
        }
        finally
        {
            if (configuration.hasChanged())
            {
                configuration.save();
            }
        }
    }
}
