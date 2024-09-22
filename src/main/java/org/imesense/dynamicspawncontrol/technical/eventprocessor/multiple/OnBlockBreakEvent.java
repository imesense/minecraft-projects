package org.imesense.dynamicspawncontrol.technical.eventprocessor.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.technical.configs.ConfigGameDebugger;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericBlockBreakActions;
import org.imesense.dynamicspawncontrol.technical.parsers.ParserJsonScripts;

/**
 *
 */
public final class OnBlockBreakEvent
{
    /**
     *
     * @param nameClass
     */
    public OnBlockBreakEvent(final String nameClass)
    {
        Log.writeDataToLogFile(0, nameClass);
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdateBlockBreakEvent_0(BlockEvent.BreakEvent event)
    {
        if (event.getWorld().isRemote)
        {
            return;
        }

        AtomicInteger i = new AtomicInteger();

        for (GenericBlockBreakActions rule : ParserJsonScripts.GENERIC_BLOCK_BREAK_ACTIONS_LIST)
        {
            if (rule.match(event))
            {
                Event.Result result = rule.getResult();

                if (ConfigGameDebugger.DebugGenericBlockBreakEvent)
                {
                    Log.writeDataToLogFile(0, "ConfigsParser._GenericBlockBreakActions. ID Rule "
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
    }
}

