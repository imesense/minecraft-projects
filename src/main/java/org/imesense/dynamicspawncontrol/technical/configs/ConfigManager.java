package org.imesense.dynamicspawncontrol.technical.configs;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class ConfigManager
{
    /**
     *
     */
    private static final Class<?>[] CONFIG_CLASSES =
    {
        SettingsLogFile.class,
        SettingsGameDebugger.class,
        SettingsWorldGenerator.class,
        SettingsRenderNight.class,
        SettingsWorldTime.class
    };

    /**
     *
     */
    public ConfigManager(final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);
    }

    /**
     *
     * @param event
     */
    public static void init(FMLPreInitializationEvent event)
    {
        for (Class<?> configClass : CONFIG_CLASSES)
        {
            try
            {
                Object configInstance = configClass.getConstructor(String.class).newInstance(configClass.getSimpleName());
                ((IConfig)configInstance).init(event, configClass.getSimpleName());
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(Log.TypeLog[2], "Exception in class: " + configClass.getName() + " - " + exception.getMessage());
            }
        }
    }
}
