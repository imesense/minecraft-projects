package org.imesense.dynamicspawncontrol.core.event.fmlnetwork;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.baseonevent.BaseOnEventInstance;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventFMLNetworkEventClientDisconnectionFromServerEvent extends BaseOnEventInstance
{
    @SubscribeEvent(priority = EventPriority.LOW)
    public void OnFMLNetworkEventClientDisconnectionFromServerEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        Log.writeDataToLogFile(0, "ClientDisconnectionFromServerEvent " + event);
    }
}
