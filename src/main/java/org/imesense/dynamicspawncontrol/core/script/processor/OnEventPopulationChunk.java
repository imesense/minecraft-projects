package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.data.SecondaryParameters1;
import org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.storage.GeneralPopulationChunkSpawn;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.data.SecondaryParameters;
import org.imesense.dynamicspawncontrol.core.script.storage.potentialspawn.storage.GeneralPotentialSpawnStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OnEventPopulationChunk
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPopulateChunk(PopulateChunkEvent.Pre event)
    {
        GeneralPopulationChunkSpawn storage = GeneralPopulationChunkSpawn.getInstance();

        List<Biome.SpawnListEntry> spawnEntries = storage.spawnEntries;
        List<SecondaryParameters1.Data> secondaryParameters = storage.secondaryParameters1;

        if (spawnEntries.isEmpty() || secondaryParameters.isEmpty())
        {
            return;
        }

        int chunkX = event.getChunkX();
        int chunkZ = event.getChunkZ();
        World world = event.getWorld();

        for (int i = 0; i < spawnEntries.size(); i++)
        {
            Biome.SpawnListEntry entry = spawnEntries.get(i);
            SecondaryParameters1.Data data = secondaryParameters.get(i);

            if (world.rand.nextFloat() < data.spawnChance)
            {
                int x = chunkX * 16 + world.rand.nextInt(16);
                int z = chunkZ * 16 + world.rand.nextInt(16);
                int y = world.getHeight(x, z);

                if (y >= data.minHeight && y <= data.maxHeight)
                {
                    for (int j = 0; j < entry.itemWeight; j++)
                    {
                        EntityLiving entity;
                        try
                        {
                            entity = entry.entityClass.getConstructor(World.class).newInstance(world);
                        }
                        catch (Exception exception)
                        {
                            Log.writeDataToLogFile(0, "Failed to spawn entity: " + exception.getMessage());
                            continue;
                        }

                        entity.setLocationAndAngles(x + 0.5, y, z + 0.5, world.rand.nextFloat() * 360.0F, 0.0F);
                        world.spawnEntity(entity);
                    }
                }
            }
        }
    }
}
