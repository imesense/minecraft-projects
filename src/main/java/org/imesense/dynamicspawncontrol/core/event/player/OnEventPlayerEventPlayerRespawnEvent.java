package org.imesense.dynamicspawncontrol.core.event.player;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.base.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.eventdescriptions.PlayerNetwork;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventPlayerEventPlayerRespawnEvent extends BaseOnEventInstance
{
    public OnEventPlayerEventPlayerRespawnEvent()
    {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnPlayerEventPlayerRespawnEvent_LOW(PlayerEvent.PlayerRespawnEvent event)
    {
        PlayerNetwork.getInstance().handlePlayerRespawn(event);
    }
}
