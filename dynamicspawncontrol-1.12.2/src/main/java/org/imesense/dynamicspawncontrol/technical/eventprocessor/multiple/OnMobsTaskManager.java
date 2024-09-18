package org.imesense.dynamicspawncontrol.technical.eventprocessor.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public final class OnMobsTaskManager
{
    /**
     *
     * @param nameClass
     */
    public OnMobsTaskManager(final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdateEntityJoinWorld_0(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof EntityLiving))
        {
            return;
        }

        /*
        AtomicInteger i = new AtomicInteger();

        for (GenericMobsTaskManager rule : ParserJsonScripts._genericMobsTaskManagerActions)
        {
            if (rule.match(event))
            {
                rule.action(event);
            }

            i.getAndIncrement();
        }
         */
    }
}
