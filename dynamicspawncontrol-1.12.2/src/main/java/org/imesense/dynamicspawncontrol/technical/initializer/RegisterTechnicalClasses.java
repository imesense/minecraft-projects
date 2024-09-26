package org.imesense.dynamicspawncontrol.technical.initializer;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.debug.events.OnEventDummy;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnPlayerEvents;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnWindowTitle;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple.*;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.script.single.*;
import org.imesense.dynamicspawncontrol.technical.worldcache.CacheEvents;

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
        CacheEvents.class,
        OnEventDummy.class,
        OnWindowTitle.class,
        OnBlockBreakEvent.class,
        OnBlockPlaceEvent.class,
        OnEntitySpawnEvent.class,
        OnLeftClickEvent.class,
        OnLivingDrops.class,
        OnLivingExperienceDrop.class,
        OnMobTaskManager.class,
        OnPlayerEvents.class,
        OnPotentialSpawns.class,
        OnRightClickEvent.class,
        OnSingleJsonCheckSpawn.class,
        OnSingleZombieSummonAID.class
    };

    /**
     *
     */
    public RegisterTechnicalClasses()
    {
        CodeGenericUtils.printInitClassToLog(RegisterTechnicalClasses.class);
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
