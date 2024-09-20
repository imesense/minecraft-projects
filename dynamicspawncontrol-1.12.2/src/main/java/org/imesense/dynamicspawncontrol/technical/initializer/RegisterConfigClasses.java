package org.imesense.dynamicspawncontrol.technical.initializer;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
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
     * @param nameClass
     */
    public RegisterConfigClasses(final String nameClass)
    {
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
                Log.writeDataToLogFile(0, "Start reading class: " + configClass.getName());

                if (!CodeGenericUtils.hasConstructorWithParameter(configClass, String.class))
                {
                    Log.writeDataToLogFile(0, "Class " + configClass.getName() + " does not have a constructor with a String parameter.");
                    throw new RuntimeException();
                }

                Object configInstance = configClass.getConstructor(String.class).newInstance(configClass.getSimpleName());

                ((IConfig)configInstance).init(event, configClass.getSimpleName());
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception in class: " + configClass.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }
}
