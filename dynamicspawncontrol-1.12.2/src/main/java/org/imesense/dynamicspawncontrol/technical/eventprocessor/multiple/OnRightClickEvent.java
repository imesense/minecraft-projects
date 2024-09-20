package org.imesense.dynamicspawncontrol.technical.eventprocessor.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public final class OnRightClickEvent
{
    /**
     *
     * @param nameClass
     */
    public OnRightClickEvent(final String nameClass)
    {
        Log.writeDataToLogFile(0, nameClass);
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdateRightClickEvent_0(PlayerInteractEvent.RightClickBlock event)
    {
        if (event.getWorld().isRemote)
        {
            return;
        }

        /*
        AtomicInteger i = new AtomicInteger();

        for (GenericRightClickActions rule : ParserJsonScripts._genericRightClickActions)
        {
            if (rule.match(event))
            {
                Event.Result result = rule.getResult();

                if (INFConfigDebug._debugGenericRightClickEvent)
                {
                    Log.writeDataToLogFile(Log._typeLog[0], "ConfigsParser._GenericRightClickActions. ID Rule: " + i + ": " + result
                            + " entity: " + event.getEntityPlayer().getName()
                            + " y: " + event.getPos().getY()
                            + " biomes: " + event.getWorld().getBiome(event.getPos()).getBiomeName());
                }

                rule.action(event);

                event.setUseBlock(result);

                if (result == Event.Result.DENY)
                {
                    event.setCanceled(true);
                }

                return;
            }

            i.getAndIncrement();
        }
         */
    }
}

