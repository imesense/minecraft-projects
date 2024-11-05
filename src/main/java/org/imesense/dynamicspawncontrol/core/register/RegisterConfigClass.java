package org.imesense.dynamicspawncontrol.core.register;

import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.config.file.BlockWorldGeneratorAConfig;
import org.imesense.dynamicspawncontrol.config.file.CacheWorldGameAConfig;
import org.imesense.dynamicspawncontrol.config.file.GameDebuggerAConfig;
import org.imesense.dynamicspawncontrol.plugin.time_control_mod_forge_1_12_2.config.CfgTimeControl;
import org.imesense.dynamicspawncontrol.config.file.LogFileAConfig;
import org.imesense.dynamicspawncontrol.config.data.PlayerAConfig;
import org.imesense.dynamicspawncontrol.plugin.darkness_forge_1_12_x_0_5_0.config.CfgDarkness;
import org.imesense.dynamicspawncontrol.config.file.SkeletonDropItemAConfig;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.config.CfgWebSlinger;
import org.imesense.dynamicspawncontrol.config.staminaplayer.CfgStaminaPlayer;
import org.imesense.dynamicspawncontrol.config.file.WindowTitleAConfig;
import org.imesense.dynamicspawncontrol.config.file.ZombieDropItemAConfig;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.lang.reflect.Constructor;

/**
 *
 */
public final class RegisterConfigClass
{
    /**
     *
     */
    private static final Class<?>[] CONFIG_CLASSES =
    {
        CacheWorldGameAConfig.class,
        GameDebuggerAConfig.class,
        LogFileAConfig.class,
        PlayerAConfig.class,
        CfgDarkness.class,
        WindowTitleAConfig.class,
        BlockWorldGeneratorAConfig.class,
        CfgTimeControl.class,
        ZombieDropItemAConfig.class,
        SkeletonDropItemAConfig.class,
        CfgWebSlinger.class,
        CfgStaminaPlayer.class
    };

    /**
     *
     */
    public RegisterConfigClass()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     */
    public static void initializeConfigs()
    {
        for (Class<?> configClass : CONFIG_CLASSES)
        {
            initializeConfig(configClass);
        }
    }

    /**
     *
     * @param _class
     * @param <T>
     */
    private static <T> void initializeConfig(Class<T> _class)
    {
        try
        {
            if (_class.isAnnotationPresent(ConceptConfig.class))
            {
                ConceptConfig dcsSingleConfig = _class.getAnnotation(ConceptConfig.class);
                String configFileName = dcsSingleConfig.fileName() + DynamicSpawnControlStructure.STRUCT_FILES_EXTENSION.SCRIPT_FILE_EXTENSION;

                Constructor<T> constructor = _class.getConstructor(String.class);
                final T INSTANCE = constructor.newInstance(configFileName);

                Log.writeDataToLogFile(0, "Initialized config: " + configFileName);
                Log.writeDataToLogFile(0, "configClass: " + _class + " " + INSTANCE);
            }
            else
            {
                Log.writeDataToLogFile(2, "No ConfigClass annotation found in: " + _class.getName());
            }
        }
        catch (NoSuchMethodException exception)
        {
            Log.writeDataToLogFile(2, "Constructor with String parameter not found in class: " + _class.getName() + " - " + exception.getMessage());
        }
        catch (Exception exception)
        {
            Log.writeDataToLogFile(2, "Exception in class: " + _class.getName() + " - " + exception.getMessage());
            throw new RuntimeException(exception);
        }
    }
}
