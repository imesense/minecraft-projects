package org.imesense.dynamicspawncontrol.core.event.worldevent;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.eventdescriptions.OvergrowingGrass;

import static org.imesense.dynamicspawncontrol.eventdescriptions.OvergrowingGrass.TICK_COUNTER;

public final class OnEventWorldEventPotentialSpawns extends BaseOnEventInstance
{
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onTickEventWorldTickEvent(TickEvent.WorldTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END || event.world.isRemote)
        {
            return;
        }

        if (TICK_COUNTER.incrementAndGet() < 20)
        {
            return;
        }

        TICK_COUNTER.set(0);

        OvergrowingGrass.getInstance().handleWorldTick(event.world);
    }
}
