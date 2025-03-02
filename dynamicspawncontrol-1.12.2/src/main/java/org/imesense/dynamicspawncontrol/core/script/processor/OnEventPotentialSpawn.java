package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventPotentialSpawn
{
    @SubscribeEvent
    public void onWorldEventPotentialSpawns_0(WorldEvent.PotentialSpawns potentialSpawns)
    {
        if (potentialSpawns.getWorld().isRemote)
        {
            return;
        }
    }
}
