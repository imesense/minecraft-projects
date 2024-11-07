package org.imesense.dynamicspawncontrol.core.register;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.event.*;
import org.imesense.dynamicspawncontrol.event.OnEventUpdateTorch;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

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
        OnEventUpdateTorch.class,
        OnEventDropZombieItem.class,
        OnEventDropSkeletonItem.class,
        OnEventComplexityBiomes.class,
        OnEventDropHeadMob.class,
        OnEventNickNameEntity.class,
        OnEventUpdateFire.class
    };

    /**
     *
     */
    public RegisterGameplayClass()
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
