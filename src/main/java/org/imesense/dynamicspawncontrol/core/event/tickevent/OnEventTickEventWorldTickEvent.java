package org.imesense.dynamicspawncontrol.core.event.tickevent;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.base.BaseOnEventInstance;
//import org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.TimeEvents;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventWorldCache;
import org.imesense.dynamicspawncontrol.eventdescriptions.OvergrowingGrass;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventTickEventWorldTickEvent extends BaseOnEventInstance
{
    public OnEventTickEventWorldTickEvent()
    {

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void OnTickEventWorldTickEvent_HIGHEST(TickEvent.WorldTickEvent event)
    {
        //TimeEvents.getInstance().handleOnWorldTick(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnTickEventWorldTickEvent_LOW(TickEvent.WorldTickEvent event)
    {
        OnEventWorldCache.getInstance().handleWorldTick(event);

        OvergrowingGrass.getInstance().handleWorldTick(event);
    }
}
