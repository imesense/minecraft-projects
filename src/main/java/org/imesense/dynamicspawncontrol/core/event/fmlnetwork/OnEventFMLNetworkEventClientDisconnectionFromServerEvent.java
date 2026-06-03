package org.imesense.dynamicspawncontrol.core.event.fmlnetwork;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.base.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventFMLNetworkEventClientDisconnectionFromServerEvent extends BaseOnEventInstance
{
    public OnEventFMLNetworkEventClientDisconnectionFromServerEvent()
    {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnFMLNetworkEventClientDisconnectionFromServerEvent_LOW(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        String playerName = "unknown";
        String disconnectReason = "unknown";

        if (UniqueField.CLIENT.player != null)
        {
            playerName = UniqueField.CLIENT.player.getName();
        }

        if (event.getManager() != null && event.getManager().getExitMessage() != null)
        {
            disconnectReason = event.getManager().getExitMessage().getUnformattedText();
        }

        String logMessage = String.format(
                "ClientDisconnectionFromServerEvent: Player '%s' disconnected from server. Reason: '%s'",
                playerName,
                disconnectReason
        );

        LogManager.info(logMessage);
    }
}
