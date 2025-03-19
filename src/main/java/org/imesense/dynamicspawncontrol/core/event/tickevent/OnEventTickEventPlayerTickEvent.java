package org.imesense.dynamicspawncontrol.core.event.tickevent;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.TimeEvents;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventTickEventPlayerTickEvent extends BaseOnEventInstance
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void OnTickEventPlayerTickEvent_HIGHEST(TickEvent.PlayerTickEvent event)
    {
        TimeEvents.getInstance().handleOnPlayerTick(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnTickEventPlayerTickEvent_LOW(TickEvent.PlayerTickEvent event)
    {

    }
}
