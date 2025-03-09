package org.imesense.dynamicspawncontrol.core.event.fmlnetwork;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventFMLNetworkEventClientConnectedToServerEvent extends BaseOnEventInstance
{
    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnFMLNetworkEventClientConnectedToServerEvent(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        String serverAddress = "unknown";
        String playerName = "unknown";

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

        Log.writeDataToLogFile(0, logMessage);
    }
}
