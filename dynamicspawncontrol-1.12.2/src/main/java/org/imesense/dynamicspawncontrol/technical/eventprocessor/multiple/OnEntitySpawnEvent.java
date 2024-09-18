package org.imesense.dynamicspawncontrol.technical.eventprocessor.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public final class OnEntitySpawnEvent
{
    /**
     *
     * @param nameClass
     */
    public OnEntitySpawnEvent(final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdateEntitySpawnEvent_0(LivingSpawnEvent.CheckSpawn event)
    {
        if (event.getWorld().isRemote)
        {
            return;
        }

        /*
        AtomicInteger i = new AtomicInteger();

        for (GenericSpawnConditions rule : ParserJsonScripts._genericSpawnConditions)
        {
            if (rule.match(event))
            {
                if (INFConfigDebug._debugGenericEntitySpawnEvent)
                {
                    Log.writeDataToLogFile(Log._typeLog[0], "ConfigsParser._GenericSpawnConditions. ID Rule: " + i + ": "
                            + "entity: " + event.getEntity().getName()
                            + " y: " + event.getY()
                            + " biomes: " + event.getWorld().getBiome(new BlockPos(event.getX(), event.getY(), event.getZ())).getBiomeName());
                }

                rule.action(event);
            }

            i.getAndIncrement();
        }
         */
    }
}

