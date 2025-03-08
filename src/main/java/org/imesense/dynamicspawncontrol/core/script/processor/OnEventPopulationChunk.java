package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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
    private EnumCreatureType getCreatureType(Class<? extends Entity> clazz)
    {
        try
        {
            EntityLiving entity = (EntityLiving) clazz.getConstructor(World.class).newInstance((World) null);

            if (entity.isCreatureType(EnumCreatureType.MONSTER, false))
            {
                return EnumCreatureType.MONSTER;
            }
            else if (entity.isCreatureType(EnumCreatureType.CREATURE, false))
            {
                return EnumCreatureType.CREATURE;
            }
            else if (entity.isCreatureType(EnumCreatureType.AMBIENT, false))
            {
                return EnumCreatureType.AMBIENT;
            }
            else if (entity.isCreatureType(EnumCreatureType.WATER_CREATURE, false))
            {
                return EnumCreatureType.WATER_CREATURE;
            }
        }
        catch (Exception exception)
        {
            Log.writeDataToLogFile(0, "Failed to determine creature type for entity: " + clazz.getName() + ", error: " + exception.getMessage());
        }


        return EnumCreatureType.CREATURE;
    }

    @SubscribeEvent
    public void onPotentialSpawn(PopulateChunkEvent.Pre event)
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
                            EnumCreatureType creatureType = getCreatureType(clazz);

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