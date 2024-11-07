package org.imesense.dynamicspawncontrol.core.register;

import net.minecraftforge.common.MinecraftForge;
import org.imesense.dynamicspawncontrol.OnEventSandBox;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.eventprocessor.script.multiple.*;
import org.imesense.dynamicspawncontrol.eventprocessor.script.single.OnSingleJsonCheckSpawn;
import org.imesense.dynamicspawncontrol.eventprocessor.script.single.OnSingleZombieSummonAID;
import org.imesense.dynamicspawncontrol.event.OnEventPlayer;
import org.imesense.dynamicspawncontrol.event.OnEventWindowTitle;
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
        OnEventSandBox.class,
        OnEventWindowTitle.class,
        OnEntitySpawnEvent.class,
        OnLivingDrop.class,
        OnLivingExperienceDrop.class,
        OnMobTaskManager.class,
        OnEventPlayer.class,
        OnPotentialSpawn.class,
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
