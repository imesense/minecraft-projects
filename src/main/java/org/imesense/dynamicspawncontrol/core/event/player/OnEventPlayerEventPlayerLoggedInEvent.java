package org.imesense.dynamicspawncontrol.core.event.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;
//import org.imesense.dynamicspawncontrol.core.plugin.mod.time_control_mod_forge_1_12_2.TimeEvents;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventWorldCache;

import java.util.UUID;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventPlayerEventPlayerLoggedInEvent extends BaseOnEventInstance
{
    public OnEventPlayerEventPlayerLoggedInEvent()
    {

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void OnPlayerEventPlayerLoggedInEvent_HIGHEST(PlayerEvent.PlayerLoggedInEvent event)
    {
        //TimeEvents.getInstance().handleOnPlayerJoin(event);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnPlayerEventPlayerLoggedInEvent_LOW(PlayerEvent.PlayerLoggedInEvent event)
    {
        EntityPlayer player = event.player;
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueID();
        BlockPos playerPos = player.getPosition();

        String logMessage = String.format(
                "PlayerLoggedInEvent: Player '%s' (UUID: %s) logged in at coordinates: X=%d, Y=%d, Z=%d",
                playerName,
                playerUUID,
                playerPos.getX(),
                playerPos.getY(),
                playerPos.getZ()
        );

        Logger.write(0, logMessage);

        OnEventWorldCache.getInstance().handlePlayerLoggedIn(event);
    }
}
