package org.imesense.dynamicspawncontrol.technical.initializer;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.debug.events.OnEventDummy;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.multiple.*;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.single.OnPlayerEvents;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.single.OnSingleJsonCheckSpawn;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.single.OnSingleZombieSummonAID;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.single.OnWindowTitle;

/**
 *
 */
public final class RegisterTechnicalClasses
{
    /**
     *
     */
    private static final Class<?>[] EVENT_CLASSES =
    {
        OnEventDummy.class,
        OnWindowTitle.class,
        OnBlockBreakEvent.class,
        OnBlockPlaceEvent.class,
        OnEntitySpawnEvent.class,
        OnLeftClickEvent.class,
        OnLivingDrops.class,
        OnLivingExperienceDrop.class,
        OnMobsTaskManager.class,
        OnPlayerEvents.class,
        OnPotentialSpawns.class,
        OnRightClickEvent.class,
        OnSingleJsonCheckSpawn.class,
        OnSingleZombieSummonAID.class
    };

    /**
     *
     * @param nameClass
     */
    public RegisterTechnicalClasses(final String nameClass)
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
                Log.writeDataToLogFile(0, "Reading class: " + eventClass.getName());

                Object eventInstance = eventClass.getConstructor(String.class).newInstance(eventClass.getSimpleName());
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
