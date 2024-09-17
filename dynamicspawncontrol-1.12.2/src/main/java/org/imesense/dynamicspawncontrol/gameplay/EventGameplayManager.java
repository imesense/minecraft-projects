package org.imesense.dynamicspawncontrol.gameplay;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.gameplay.events.OnUpdateTorchLogic;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public final class EventGameplayManager
{
    /**
     *
     */
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnUpdateTorchLogic.class
    };

    /**
     *
     */
    public EventGameplayManager(final String nameClass)
    {

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
                Object eventInstance = eventClass.getConstructor(String.class).newInstance(eventClass.getSimpleName());
                MinecraftForge.EVENT_BUS.register(eventInstance);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(Log.TypeLog[2], "Exception in class: " + eventClass.getName() + " - " + exception.getMessage());
            }
        }
    }
}
