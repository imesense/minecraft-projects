package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.storage.GeneralPotentialSpawnStorage;

import java.util.List;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventPotentialSpawn
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPotentialSpawns(WorldEvent.PotentialSpawns event)
    {
        List<Biome.SpawnListEntry> spawnEntries = GeneralPotentialSpawnStorage.getInstance().spawnEntries;

        if (spawnEntries.isEmpty())
        {
            return;
        }

        for (Biome.SpawnListEntry entry : spawnEntries)
        {
            event.getList().add(entry);
        }
    }
}