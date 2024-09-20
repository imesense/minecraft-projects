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
                Log.writeDataToLogFile(Log.TypeLog[0], "---------------------------------------------");
                Log.writeDataToLogFile(Log.TypeLog[0], "Start reading class: " + configClass.getName());

                if (!hasConstructorWithParameter(configClass, String.class))
                {
                    throw new NoSuchMethodException("Class " + configClass.getName() + " does not have a constructor with a String parameter.");
                }

                Object configInstance = configClass.getConstructor(String.class).newInstance(configClass.getSimpleName());

                ((IConfig)configInstance).init(event, configClass.getSimpleName());

                Log.writeDataToLogFile(Log.TypeLog[0], String.format("Config instance created: %s", configInstance));

                Log.writeDataToLogFile(Log.TypeLog[0], "End reading class: " + configClass.getName());
                Log.writeDataToLogFile(Log.TypeLog[0], "---------------------------------------------");
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(Log.TypeLog[2], "Exception in class: " + configClass.getName() + " - " + exception.getMessage());
            }
        }
    }

    private static boolean hasConstructorWithParameter(Class<?> clazz, Class<?>... parameterTypes)
    {
        try
        {
            clazz.getConstructor(parameterTypes);
            return true;
        }
        catch (NoSuchMethodException e)
        {
            return false;
        }
    }
}
