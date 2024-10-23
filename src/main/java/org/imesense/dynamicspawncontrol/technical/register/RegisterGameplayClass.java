package org.imesense.dynamicspawncontrol.technical.register;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.gameplay.event.OnUpdateTorchLogic;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnComplexityBiomes;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnDropSkeletonItem;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnDropZombieItem;

/**
 *
 */
public final class RegisterGameplayClass
{
    /**
     *
     */
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnUpdateTorchLogic.class,
        OnDropZombieItem.class,
        OnDropSkeletonItem.class,
        OnComplexityBiomes.class
    };

    /**
     *
     */
    public RegisterGameplayClass()
    {
        CodeGenericUtil.printInitClassToLog(this.getClass());
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
