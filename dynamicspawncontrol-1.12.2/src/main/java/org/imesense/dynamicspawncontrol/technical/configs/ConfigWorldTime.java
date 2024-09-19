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
public final class ConfigWorldTime implements IConfig
{
    /**
     *
     */
    public static int DayLengthMinutes = 10;

    /**
     *
     */
    public static int NightLengthMinutes = 10;

    /**
     *
     */
    public static int SyncToSystemTimeRate = 20;

    /**
     *
     */
    public static boolean TimeControlDebug = false;

    /**
     *
     */
    public static boolean SyncToSystemTime = false;

    /**
     *
     * @param nameClass
     */
    public ConfigWorldTime(final String nameClass)
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

        ClientProxy.ConfigWorldTime = this.createConfiguration("world_time");

        this.read();
    }

    /**
     *
     * @param configuration
     */
    @Override
    public void readProperties(Configuration configuration)
    {
        TimeControlDebug = this.getConfigValueB(
                configuration,
                "TimeControlDebug",
                "game_time",
                TimeControlDebug,
                "Debugging information about the current time"
        );

        SyncToSystemTime = this.getConfigValueB(
                configuration,
                "SyncToSystemTime",
                "game_time",
                SyncToSystemTime,
                "Synchronize in-world time with system time"
        );

        SyncToSystemTimeRate = this.getConfigValueI(
                configuration,
                "SyncToSystemTimeRate",
                "game_time",
                SyncToSystemTimeRate,
                1,
                864000,
                "Sync time every n ticks"
        );

        DayLengthMinutes = this.getConfigValueI(
                configuration,
                "DayLengthMinutes",
                "game_time",
                DayLengthMinutes,
                0,
                12000,
                "How long daytime lasts"
        );

        NightLengthMinutes = this.getConfigValueI(
                configuration,
                "NightLengthMinutes",
                "game_time",
                NightLengthMinutes,
                0,
                12000,
                "How long nighttime lasts"
        );
    }

    /**
     *
     */
    @Override
    public void read()
    {
        Configuration configuration = ClientProxy.ConfigWorldTime;

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
