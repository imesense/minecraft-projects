package org.imesense.dynamicspawncontrol.core.event.entity;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.eventdescriptions.PlayerNetwork;
import org.imesense.dynamicspawncontrol.eventdescriptions.UpdateFire;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventEntityJoinWorldEvent extends BaseOnEventInstance
{
    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnEntityJoinWorldEvent(EntityJoinWorldEvent event)
    {
        PlayerNetwork.getInstance().handlePlayerJoinWorld(event);

        UpdateFire.getInstance().handleEntityJoinWorld(event);
    }
}
