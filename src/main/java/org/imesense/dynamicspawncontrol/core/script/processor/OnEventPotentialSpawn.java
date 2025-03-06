package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.data.SecondaryParameters;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.storage.GeneralPotentialSpawnStorage;

import java.util.List;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventPotentialSpawn
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPotentialSpawns(WorldEvent.PotentialSpawns event)
    {
        GeneralPotentialSpawnStorage storage = GeneralPotentialSpawnStorage.getInstance();
        List<Biome.SpawnListEntry> spawnEntries = storage.spawnEntries;
        List<SecondaryParameters.Data> secondaryParameters = storage.secondaryParameters;

        if (spawnEntries.isEmpty() || secondaryParameters.isEmpty())
        {
            return;
        }

        int eventY = event.getPos().getY();

        for (int i = 0; i < spawnEntries.size(); i++)
        {
            Biome.SpawnListEntry entry = spawnEntries.get(i);
            SecondaryParameters.Data data = secondaryParameters.get(i);

            if (UniqueField.RANDOM.nextFloat() < data.spawnChance && eventY >= data.minHeight && eventY <= data.maxHeight)
            {
                event.getList().add(entry);
            }
        }
    }
}