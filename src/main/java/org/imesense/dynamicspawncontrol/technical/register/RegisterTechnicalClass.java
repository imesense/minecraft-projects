package org.imesense.dynamicspawncontrol.technical.register;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.debug.event.OnEventDummy;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnPlayerEvent;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnWindowTitle;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple.*;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.script.single.*;
import org.imesense.dynamicspawncontrol.technical.worldcache.CacheEvent;

/**
 *
 */
public final class RegisterTechnicalClass
{
    /**
     *
     */
    private static final Class<?>[] EVENT_CLASSES =
    {
        CacheEvent.class,
        OnEventDummy.class,
        OnWindowTitle.class,
        OnBlockBreakEvent.class,
        OnBlockPlaceEvent.class,
        OnEntitySpawnEvent.class,
        OnLeftClickEvent.class,
        OnLivingDrop.class,
        OnLivingExperienceDrop.class,
        OnMobTaskManager.class,
        OnPlayerEvent.class,
        OnPotentialSpawn.class,
        OnRightClickEvent.class,
        OnSingleJsonCheckSpawn.class,
        OnSingleZombieSummonAID.class
    };

    /**
     *
     */
    public RegisterTechnicalClass()
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
