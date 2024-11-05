package org.imesense.dynamicspawncontrol.core.register;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.OnEventDummy;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnPlayerEvent;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive.OnWindowTitle;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple.*;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.script.single.*;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheEvent;

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
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    /**
     *
     */
    public static void registerClasses()
    {
        for (Class<?> _class : EVENT_CLASSES)
        {
            try
            {
                Object object =
                        _class.getConstructor().newInstance();

                MinecraftForge.EVENT_BUS.register(object);
            }
            catch (Exception exception)
            {
                Log.writeDataToLogFile(2, "Exception in class: " + _class.getName() + " - " + exception.getMessage());
                throw new RuntimeException(exception);
            }
        }
    }
}
