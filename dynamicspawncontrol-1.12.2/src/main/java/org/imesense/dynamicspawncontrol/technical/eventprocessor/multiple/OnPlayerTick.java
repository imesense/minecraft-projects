package org.imesense.dynamicspawncontrol.technical.eventprocessor.multiple;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public final class OnPlayerTick
{
    /**
     *
     */
    private final static Map<Integer, Integer> tickCounters = new HashMap<>();

    /**
     *
     * @param nameClass
     */
    public OnPlayerTick(final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdatePlayerTick_0(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END || event.side != Side.SERVER)
        {
            return;
        }

        int id = event.player.getEntityId();

        tickCounters.putIfAbsent(id, 0);

        int tickCounter = tickCounters.get(id) + 1;
        tickCounters.put(id, tickCounter);

        /*
        AtomicInteger i = new AtomicInteger();

        for (GenericMapEffectsActions rule : ParserJsonScripts._genericMapEffectsActions)
        {
            if (tickCounter % rule.getTimeout() == 0 && rule.match(event))
            {
                if (INFConfigDebug._debugGenericPlayerTick)
                {
                    Log.writeDataToLogFile(Log._typeLog[0], "ConfigsParser._GenericMapEffectsActions. ID Rule: " + i
                            + " entity: " + event.player.getName()
                            + " y: " + event.player.getPosition().getY());
                }

                rule.action(event);

                return;
            }

            i.getAndIncrement();
        }
         */
    }
}

