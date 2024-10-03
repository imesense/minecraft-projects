package org.imesense.dynamicspawncontrol.technical.eventprocessor.script.multiple;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.config.gamedebugger.DataGameDebugger;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.generic.GenericLeftClickActions;
import org.imesense.dynamicspawncontrol.technical.parsers.ParserGenericJsonScripts;

/**
 *
 */
@Mod.EventBusSubscriber
public final class OnLeftClickEvent
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnLeftClickEvent()
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
    public synchronized void onUpdateLeftClickEvent_0(PlayerInteractEvent.LeftClickBlock event)
    {
        if (event.getWorld().isRemote)
        {
            return;
        }

        AtomicInteger i = new AtomicInteger();

        for (GenericLeftClickActions rule : ParserGenericJsonScripts.GENERIC_LEFT_CLICK_ACTIONS_LIST)
        {
            if (rule.match(event))
            {
                Event.Result result = rule.getResult();

                if (DataGameDebugger.ConfigDataEvent.instance.getDebugSetting("debug_on_left_click"))
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

