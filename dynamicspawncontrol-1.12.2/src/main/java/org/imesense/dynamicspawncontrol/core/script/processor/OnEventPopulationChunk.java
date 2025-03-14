package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.storage.GeneralPopulationChunkSpawn;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;

public final class OnEventPopulationChunk
{
    private static volatile OnEventPopulationChunk _INSTANCE;

    public static OnEventPopulationChunk getInstance()
    {
        return CodeGeneric.getInstance(OnEventPopulationChunk.class);
    }

    public void handlePotentialSpawn(PopulateChunkEvent.Pre event)
    {
        List<GeneralPopulationChunkSpawn.Data> populationList = GeneralPopulationChunkSpawn.getInstance().populationList;

        if (populationList != null)
        {
            for (GeneralPopulationChunkSpawn.Data data : populationList)
            {
                EntityEntry ee = ForgeRegistries.ENTITIES.getValue(data.entity);

                if (ee != null)
                {
                    Class<? extends Entity> clazz = ee.getEntityClass();

                    if (clazz != null)
                    {
                        float minChance;
                        float maxChance;

                        switch (data.spawnChancePriority.toLowerCase())
                        {
                            case "low":
                                minChance = 0.01f;
                                maxChance = 0.25f;
                                break;
                            case "medium":
                                minChance = 0.25f;
                                maxChance = 0.50f;
                                break;
                            case "high":
                                minChance = 0.75f;
                                maxChance = 1.00f;
                                break;
                            default:
                                minChance = 0.25f;
                                maxChance = 0.50f;
                                break;
                        }

                        float randomChance = minChance + event.getWorld().rand.nextFloat() * (maxChance - minChance);

                        if (event.getWorld().rand.nextFloat() < randomChance)
                        {
                            EnumCreatureType creatureType = CodeGeneric.getCreatureType(clazz);

                            for (Biome biome : Biome.REGISTRY)
                            {
                                List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(creatureType);

                                int currentEntitiesInChunk = (int) spawnList.stream()
                                        .filter(entry -> entry.entityClass.equals(clazz))
                                        .count();

                                if (currentEntitiesInChunk < data.maxEntitiesPerChunk)
                                {
                                    spawnList.add(new Biome.SpawnListEntry((Class<? extends EntityLiving>) clazz,
                                                    data.weight, data.groupCountMin, data.groupCountMax)
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}