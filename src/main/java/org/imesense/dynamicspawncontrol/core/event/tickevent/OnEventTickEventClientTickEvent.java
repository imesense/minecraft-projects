package org.imesense.dynamicspawncontrol.core.event.tickevent;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.memory.MemoryEvents;
//import org.imesense.dynamicspawncontrol.core.plugin.mod.void_fog_1_12_1_1_2.FogEvent;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventTickEventClientTickEvent extends BaseOnEventInstance
{
    public OnEventTickEventClientTickEvent()
    {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnTickEventClientTickEvent_LOW(TickEvent.ClientTickEvent event)
    {
        MemoryEvents.handleOnClientTick(event);

        //FogEvent.getInstance().handleFogVoidParticles(event);
    }
}
