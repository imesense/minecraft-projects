package org.imesense.dynamicspawncontrol.technical.initializer;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.configs.*;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class RegisterCfgClasses {
    private static final Class<?>[] CONFIG_CLASSES = {
            //CfgRenderNightConfig.class,
            //CfgCacheWorldGame.class,
            CfgGameDebugger.class//,
            //CfgLogFile.class,
            //CfgPlayer.class,
            //CfgWindowTitle.class,
            //CfgWorldGenerator.class,
            //CfgWorldTime.class,
            //CfgZombieDropItem.class
    };

    // Массив имен файлов конфигурации
    private static final String[] CONFIG_FILE_NAMES = {
            //"darkness_config.json",
            //"cache_world_game_config.json",
            "game_debugger_config.json"//,
            //"log_file_config.json",
            //"player_config.json",
            //"window_config.json",
            //"ore_generator_config.json",
            //"world_time_config.json",
            //"zombie_drop_config.json"
    };

    public RegisterCfgClasses() {
        CodeGenericUtils.printInitClassToLog(RegisterCfgClasses.class);
    }

    public static void initializeConfigs() {
        for (int i = 0; i < CONFIG_CLASSES.length; i++) {
            initializeConfig(CONFIG_CLASSES[i], CONFIG_FILE_NAMES[i]);
        }
    }

    private static <T> void initializeConfig(Class<T> configClass, String configFileName) {
        try {
            Constructor<T> constructor = configClass.getConstructor(String.class);
            T configInstance = constructor.newInstance(configFileName);

            Log.writeDataToLogFile(0, "Test: " + configInstance);

            Log.writeDataToLogFile(0, "configClass: " + configClass + " " + configInstance);

        } catch (NoSuchMethodException e) {
            Log.writeDataToLogFile(2, "Constructor with String parameter not found in class: " + configClass.getName() + " - " + e.getMessage());
        } catch (Exception e) {
            Log.writeDataToLogFile(2, "Exception in class: " + configClass.getName() + " - " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
