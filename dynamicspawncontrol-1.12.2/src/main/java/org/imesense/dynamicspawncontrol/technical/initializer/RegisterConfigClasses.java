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
        //ConfigWindowTitle.class,
        //ConfigLogFile.class,
        //ConfigGameDebugger.class,
        //ConfigWorldGenerator.class,
        //ConfigRenderNight.class,
        //ConfigWorldTime.class,
        //ConfigPlayer.class,
        //ConfigZombieDropItem.class
    };

    /**
     *
     */
    public RegisterConfigClasses()
    {
        CodeGenericUtils.printInitClassToLog(RegisterConfigClasses.class);
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
                if (!CodeGenericUtils.hasDefaultConstructor(configClass))
                {
                    Log.writeDataToLogFile(2, "Class " + configClass.getName() + " does not have a default constructor.");
                    throw new RuntimeException("Default constructor not found in class: " + configClass.getName());
                }

                Object configInstance =
                        configClass.getConstructor().newInstance();

                ((IConfig)configInstance).init(event);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception in class: " + configClass.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }
}
