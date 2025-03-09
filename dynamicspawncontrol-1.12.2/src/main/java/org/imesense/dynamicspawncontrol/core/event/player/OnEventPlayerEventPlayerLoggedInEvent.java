package org.imesense.dynamicspawncontrol.core.event.player;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventPlayerEventPlayerLoggedInEvent extends BaseOnEventInstance
{
    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnPlayerEventPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        Log.writeDataToLogFile(0, "PlayerLoggedInEvent " + event.player.getName() + " logged in.");
    }
}
