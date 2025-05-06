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
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.processor.OnEventWorldCache;
import org.imesense.dynamicspawncontrol.eventdescriptions.PlayerNetwork;

import java.util.UUID;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventPlayerEventPlayerLoggedOutEvent extends BaseOnEventInstance
{
    public OnEventPlayerEventPlayerLoggedOutEvent()
    {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnPlayerEventPlayerLoggedOutEvent_LOW(PlayerEvent.PlayerLoggedOutEvent event)
    {
        EntityPlayer player = event.player;
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueID();
        BlockPos playerPos = player.getPosition();

        String logMessage = String.format(
                "PlayerLoggedOutEvent: Player '%s' (UUID: %s) logged out at coordinates: X=%d, Y=%d, Z=%d",
                playerName,
                playerUUID,
                playerPos.getX(),
                playerPos.getY(),
                playerPos.getZ()
        );

        Log.write(0, logMessage);

        PlayerNetwork.getInstance().handlePlayerLoggedOut(event);

        OnEventWorldCache.getInstance().handlePlayerLoggedOut(event);
    }
}
