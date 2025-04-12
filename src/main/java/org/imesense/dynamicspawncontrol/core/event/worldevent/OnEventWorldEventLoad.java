package org.imesense.dynamicspawncontrol.core.event.worldevent;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.TimeEvents;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventWorldEventLoad extends BaseOnEventInstance
{
    public OnEventWorldEventLoad()
    {

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void OnWorldEventLoad_HIGHEST(WorldEvent.Load event)
    {
        TimeEvents.getInstance().handleOnWorldLoad(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnWorldEventLoad_LOW(WorldEvent.Load event)
    {

    }
}
