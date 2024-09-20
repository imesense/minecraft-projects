package org.imesense.dynamicspawncontrol.technical.eventprocessor.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public final class OnBlockPlaceEvent
{
    /**
     *
     * @param nameClass
     */
    public OnBlockPlaceEvent(final String nameClass)
    {
        Log.writeDataToLogFile(0, nameClass);
    }

    /**
     *
     * @param event
     */
    @Deprecated
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdateBlockPaceEvent_0(BlockEvent.PlaceEvent event)
    {
        if (event.getWorld().isRemote)
        {
            return;
        }

        /*
        AtomicInteger i = new AtomicInteger();

        for (GenericBlockPlaceActions rule : ParserJsonScripts._genericBlockPlaceActions)
        {
            if (rule.match(event))
            {
                Event.Result result = rule.getResult();

                if (INFConfigDebug._debugGenericBlockPlaceEvent)
                {
                    Log.writeDataToLogFile(Log._typeLog[0], "ConfigsParser._GenericBlockPlaceActions. ID Rule "
                            + i + ": "
                            + result + " entity: "
                            + event.getPlayer().getName()
                            + " y: " + event.getPos().getY()
                            + " biomes: "
                            + event.getWorld().getBiome(event.getPos()).getBiomeName());
                }

                rule.action(event);

                if (result == Event.Result.DENY)
                {
                    event.setCanceled(true);
                }
            }

            i.getAndIncrement();
        }
         */
    }
}

