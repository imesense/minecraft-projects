package org.imesense.dynamicspawncontrol.core.register;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.plugin.fogworld_1_12_1_1_0_b15_universal.handler.FogEventHandler;
import org.imesense.dynamicspawncontrol.plugin.void_fog_1_12_1_1_2.FogEvent;

/**
 *
 */
public class RegisterRenderClass
{
    /**
     *
     */
    private static final Class<?>[] EVENT_CLASSES =
    {
        FogEvent.class,
        FogEventHandler.class
    };

    /**
     *
     */
    public RegisterRenderClass()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     */
    public static void registerClasses()
    {
        for (Class<?> eventClass : EVENT_CLASSES)
        {
            try
            {
                Object object =
                        eventClass.getConstructor().newInstance();

                MinecraftForge.EVENT_BUS.register(object);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception in class: " + eventClass.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }
}
