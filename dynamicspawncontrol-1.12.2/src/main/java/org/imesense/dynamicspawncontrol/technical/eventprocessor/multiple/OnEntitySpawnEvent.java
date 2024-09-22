package org.imesense.dynamicspawncontrol.technical.eventprocessor.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.technical.configs.ConfigGameDebugger;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericSpawnConditions;
import org.imesense.dynamicspawncontrol.technical.parsers.ParserJsonScripts;

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
        Log.writeDataToLogFile(0, nameClass);
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

        AtomicInteger i = new AtomicInteger();

        for (GenericSpawnConditions rule : ParserJsonScripts.GENERIC_SPAWN_CONDITIONS_LIST)
        {
            if (rule.match(event))
            {
                if (ConfigGameDebugger.DebugGenericEntitySpawnEvent)
                {
                    Log.writeDataToLogFile(0, "ConfigsParser._GenericSpawnConditions. ID Rule: " + i + ": "
                            + "entity: " + event.getEntity().getName()
                            + " y: " + event.getY()
                            + " biomes: " + event.getWorld().getBiome(new BlockPos(event.getX(), event.getY(), event.getZ())).getBiomeName());
                }

                rule.action(event);
            }

            i.getAndIncrement();
        }
    }
}

