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
public final class ConfigGameDebugger implements IConfig
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
    public ConfigGameDebugger(final String nameClass)
    {

    }

    /**
     *
     * @param event
     */
    @Override
    public void init(FMLPreInitializationEvent event)
    {
        ClientProxy.ConfigGameDebugger = this.createConfiguration("game_debugger");

        this.read();
    }

    /**
     *
     * @param configuration
     */
    @Override
    public void readProperties(Configuration configuration)
    {
        DebugMonitorCache = this.getConfigValueB(
                configuration,
                "Debug info cache",
                "monitor_debug",
                DebugMonitorCache,
                "Translates the cache parameters to the screen in static text"
        );

        DebugGenericPlayerTick = this.getConfigValueB(
                configuration,
                "Debug EventEffects",
                "generic_maps_debug",
                DebugGenericPlayerTick,
                "Debugging the 'EventEffects.json'"
        );

        DebugGenericLivingDrops = this.getConfigValueB(
                configuration,
                "Debug DropAllItems",
                "generic_maps_debug",
                DebugGenericLivingDrops,
                "Debugging the 'DropAllItems.json'"
        );

        DebugGenericSummonAidEvent = this.getConfigValueB(
                configuration,
                "Debug ZombieSummonAid",
                "generic_maps_debug",
                DebugGenericSummonAidEvent,
                "Debugging the 'ZombieSummonAid.json'"
        );

        DebugGenericLeftClickEvent = this.getConfigValueB(
                configuration,
                "Debug EventLeftMouseClick",
                "generic_maps_debug",
                DebugGenericLeftClickEvent,
                "Debugging the 'EventLeftMouseClick.json'"
        );

        DebugGenericPotentialSpawns = this.getConfigValueB(
                configuration,
                "Debug MainOverrideSpawn",
                "generic_maps_debug",
                DebugGenericPotentialSpawns,
                "Debugging the 'MainOverrideSpawn.json'"
        );

        DebugGenericRightClickEvent = this.getConfigValueB(
                configuration,
                "Debug EventRightMouseClick",
                "generic_maps_debug",
                DebugGenericRightClickEvent,
                "Debugging the 'EventRightMouseClick.json'"
        );

        DebugGenericBlockBreakEvent = this.getConfigValueB(
                configuration,
                "Debug EventBlockBreak",
                "generic_maps_debug",
                DebugGenericBlockBreakEvent,
                "Debugging the 'EventBlockBreak.json'"
        );

        DebugGenericBlockPlaceEvent = this.getConfigValueB(
                configuration,
                "Debug EventBlockPlace",
                "generic_maps_debug",
                DebugGenericBlockPlaceEvent,
                "Debugging the 'EventBlockPlace.json'"
        );

        DebugGenericEntitySpawnEvent = this.getConfigValueB(
                configuration,
                "Debug SpawnConditions",
                "generic_maps_debug",
                DebugGenericEntitySpawnEvent,
                "Debugging the 'SpawnConditions.json'"
        );

        DebugGenericLivingExperienceDrop = this.getConfigValueB(
                configuration,
                "Debug DropAllExperience",
                "generic_maps_debug",
                DebugGenericLivingExperienceDrop,
                "Debugging the 'DropAllExperience.json'"
        );
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
