package org.imesense.dynamicspawncontrol.technical.initializer;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.gameplay.events.OnUpdateTorchLogic;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnDropZombieItems;

/**
 *
 */
public final class RegisterGameplayClasses
{
    /**
     *
     */
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnUpdateTorchLogic.class,
        OnDropZombieItems.class
    };

    /**
     *
     */
    public RegisterGameplayClasses()
    {
        CodeGenericUtils.printInitClassToLog(this.getClass());
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
                Object eventInstance =
                        eventClass.getConstructor().newInstance();

                MinecraftForge.EVENT_BUS.register(eventInstance);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception in class: " + eventClass.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }
}
