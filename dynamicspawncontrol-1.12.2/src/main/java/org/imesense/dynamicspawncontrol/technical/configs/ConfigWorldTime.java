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

        ClientProxy.ConfigWorldTime = new Configuration(new File(DynamicSpawnControl.getGlobalPathToConfigs().getPath() +
                File.separator + DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIRECTORY +
                File.separator + DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_CONFIGS, "world_time" +
                DynamicSpawnControl.STRUCT_FILES_EXTENSION.CONFIG_FILE_EXTENSION));

        this.read();
    }

    /**
     *
     * @param configuration
     */
    @Override
    public void readProperties(Configuration configuration)
    {
        TimeControlDebug =
                configuration.getBoolean("TimeControlDebug", "game_time", TimeControlDebug,
                        "Debugging information about the current time");

        SyncToSystemTime =
                configuration.getBoolean("SyncToSystemTime", "game_time", SyncToSystemTime,
                        "Synchronize in-world time with system time");

        SyncToSystemTimeRate =
                configuration.getInt("SyncToSystemTimeRate", "game_time", SyncToSystemTimeRate, 1, 864000,
                        "Sync time every n ticks");

        DayLengthMinutes =
                configuration.getInt("DayLengthMinutes", "game_time", DayLengthMinutes, 0, 12000,
                        "How long daytime lasts");

        NightLengthMinutes =
                configuration.getInt("NightLengthMinutes", "game_time", NightLengthMinutes, 0, 12000,
                        "How long nighttime lasts");
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