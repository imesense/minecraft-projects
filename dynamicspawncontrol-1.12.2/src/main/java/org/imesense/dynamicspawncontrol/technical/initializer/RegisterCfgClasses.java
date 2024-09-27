package org.imesense.dynamicspawncontrol.technical.initializer;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.configs.*;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public final class RegisterCfgClasses
{
    /**
     *
     */
    private static final Class<?>[] CONFIG_CLASSES =
    {
        CfgRenderNightConfig.class,
        CfgCacheWorldGame.class,
        CfgGameDebugger.class,
        CfgLogFile.class,
        CfgPlayer.class,
        CfgWindowTitle.class,
        CfgWorldGenerator.class,
        CfgWorldTime.class,
        CfgZombieDropItem.class
    };

    /**
     *
     */
    private static final String[] CONFIG_FILE_NAMES =
    {
        "darkness_config.json",
        "cache_world_game_config.json",
        "game_debugger_config.json",
        "log_file_config.json",
        "player_config.json",
        "window_config.json",
        "ore_generator_config.json",
        "world_time_config.json",
        "zombie_drop_config.json"
    };

    /**
     *
     */
    public RegisterCfgClasses()
    {
        CodeGenericUtils.printInitClassToLog(RegisterCfgClasses.class);
    }

    /**
     *
     */
    public static void initializeConfigs()
    {
        for (int i = 0; i < CONFIG_CLASSES.length; i++)
        {
            Class<?> configClass = CONFIG_CLASSES[i];
            String configFileName = CONFIG_FILE_NAMES[i];

            try
            {
                Object configInstance =
                        configClass.getConstructor(String.class).newInstance(configFileName);

                configClass.getField("instance").set(null, configInstance);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception in class: " + configClass.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }
}
