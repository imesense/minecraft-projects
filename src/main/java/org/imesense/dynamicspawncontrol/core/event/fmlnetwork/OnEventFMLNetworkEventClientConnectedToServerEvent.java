package org.imesense.dynamicspawncontrol.core.event.fmlnetwork;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

@InitLog
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventFMLNetworkEventClientConnectedToServerEvent extends BaseOnEventInstance
{
    public OnEventFMLNetworkEventClientConnectedToServerEvent()
    {

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnFMLNetworkEventClientConnectedToServerEvent_LOW(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        String playerName = "unknown";
        String serverAddress = "unknown";

        if (event.getManager() != null && event.getManager().getRemoteAddress() != null)
        {
            serverAddress = event.getManager().getRemoteAddress().toString();
        }

        if (UniqueField.CLIENT.player != null)
        {
            playerName = UniqueField.CLIENT.player.getName();
        }

        String logMessage = String.format(
                "ClientConnectedToServerEvent: Player '%s' connected to server '%s'",
                playerName,
                serverAddress
        );

        Log.write(0, logMessage);
    }
}
