package org.imesense.dynamicspawncontrol.technical.initializer;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.technical.configs.*;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public final class RegisterConfigClasses
{
    /**
     *
     */
    private static final Class<?>[] CONFIG_CLASSES =
    {
        ConfigLogFile.class,
        ConfigGameDebugger.class,
        ConfigWorldGenerator.class,
        ConfigRenderNight.class,
        ConfigWorldTime.class,
        ConfigPlayer.class,
        ConfigZombieDropItem.class,
        ConfigDebugSingleEvents.class
    };

    /**
     *
     */
    public RegisterConfigClasses(final String nameClass)
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

                Log.writeDataToLogFile(Log.TypeLog[0], String.format("configInstance (%s)", configInstance));
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(Log.TypeLog[2], "Exception in class: " + configClass.getName() + " - " + exception.getMessage());
            }
        }
    }
}
