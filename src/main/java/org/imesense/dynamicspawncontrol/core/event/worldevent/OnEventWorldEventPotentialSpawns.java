package org.imesense.dynamicspawncontrol.core.event.worldevent;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventPotentialSpawnOld;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventWorldEventPotentialSpawns extends BaseOnEventInstance
{
    public OnEventWorldEventPotentialSpawns()
    {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPotentialSpawns_LOW(WorldEvent.PotentialSpawns event)
    {
        if (!event.getWorld().isRemote)
        {
            OnEventPotentialSpawnOld.getInstance().handlePotentialSpawns(event);
        }
    }
}
