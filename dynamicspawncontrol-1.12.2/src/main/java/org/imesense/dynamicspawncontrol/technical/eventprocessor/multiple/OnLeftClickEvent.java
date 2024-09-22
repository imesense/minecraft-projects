package org.imesense.dynamicspawncontrol.technical.eventprocessor.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.imesense.dynamicspawncontrol.technical.configs.ConfigGameDebugger;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericLeftClickActions;
import org.imesense.dynamicspawncontrol.technical.parsers.ParserJsonScripts;

/**
 *
 */
public final class OnLeftClickEvent
{
    /**
     *
     * @param nameClass
     */
    public OnLeftClickEvent(final String nameClass)
    {
        Log.writeDataToLogFile(0, nameClass);
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdateLeftClickEvent_0(PlayerInteractEvent.LeftClickBlock event)
    {
        if (event.getWorld().isRemote)
        {
            return;
        }

        AtomicInteger i = new AtomicInteger();

        for (GenericLeftClickActions rule : ParserJsonScripts.GENERIC_LEFT_CLICK_ACTIONS_LIST)
        {
            if (rule.match(event))
            {
                Event.Result result = rule.getResult();

                if (ConfigGameDebugger.DebugGenericLeftClickEvent)
                {
                    Log.writeDataToLogFile(0, "ConfigsParser._GenericLeftClickActions. ID Rule: " + i + ": "
                            + result
                            + " entity: " + event.getEntityPlayer().getName()
                            + " y: " + event.getPos().getY()
                            + " biome: " + event.getWorld().getBiome(event.getPos()).getBiomeName());
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
    }
}

