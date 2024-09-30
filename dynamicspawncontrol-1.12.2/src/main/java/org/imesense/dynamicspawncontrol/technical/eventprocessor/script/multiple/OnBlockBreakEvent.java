package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;

import org.imesense.dynamicspawncontrol.technical.config.gamedebugger.DataGameDebugger;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericBlockBreakActions;
import org.imesense.dynamicspawncontrol.technical.parsers.ParserGenericJsonScripts;

/**
 *
 */
@Mod.EventBusSubscriber
public final class OnBlockBreakEvent
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnBlockBreakEvent()
    {
		CodeGenericUtils.printInitClassToLog(this.getClass());
		
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
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

        for (GenericBlockBreakActions rule : ParserGenericJsonScripts.GENERIC_BLOCK_BREAK_ACTIONS_LIST)
        {
            if (rule.match(event))
            {
                Event.Result result = rule.getResult();

                if (DataGameDebugger.DebugEvent.instance.getDebugOnBlockBreak())
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

