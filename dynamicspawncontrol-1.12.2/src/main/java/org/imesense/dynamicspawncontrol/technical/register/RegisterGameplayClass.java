package org.imesense.dynamicspawncontrol.technical.register;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.gameplay.event.OnUpdateTorchLogic;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.*;

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
        OnComplexityBiomes.class,
        OnDropHeadMob.class,
        OnNickNameEntity.class
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
