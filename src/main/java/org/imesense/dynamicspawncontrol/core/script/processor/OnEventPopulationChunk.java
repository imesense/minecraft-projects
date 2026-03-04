package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.data.PopulationChunkStruct;
import org.imesense.dynamicspawncontrol.core.script.storage.populationchunk.storage.GeneralPopulationChunkSpawn;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.List;

@InitLog
@TODO(value = "Поломана оптимизация, к тому же переделать класс на схеме", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class OnEventPopulationChunk
{
    private static volatile OnEventPopulationChunk _INSTANCE;

    public static OnEventPopulationChunk getInstance()
    {
        return CodeGeneric.getInstance(OnEventPopulationChunk.class);
    }

    public OnEventPopulationChunk()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handlePopulateChunkEventPre(PopulateChunkEvent.Pre event)
    {
        List<PopulationChunkStruct.Data> populationList = GeneralPopulationChunkSpawn.getInstance().populationChunkStruct;

        if (populationList != null)
        {
            int currentDimension = event.getWorld().provider.getDimension();

            for (PopulationChunkStruct.Data data : populationList)
            {
                if (data.idDimension != null && data.idDimension != currentDimension)
                {
                    continue;
                }

                EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(data.entity);

                if (entityEntry != null)
                {
                    Class<? extends Entity> _class = entityEntry.getEntityClass();

                    if (_class != null)
                    {
                        if (data.biomes != null && !data.biomes.isEmpty())
                        {
                            BlockPos pos = new BlockPos(event.getChunkX() * 16, 64, event.getChunkZ() * 16);
                            Biome currentBiome = event.getWorld().getBiome(pos);

                            String currentBiomeName = currentBiome.getRegistryName().toString();
                            String currentBiomeSimpleName = currentBiomeName.replace("minecraft:", "");

                            boolean isBiomeValid = false;

                            for (String biome : data.biomes)
                            {
                                if (biome.equalsIgnoreCase(currentBiomeName) || biome.equalsIgnoreCase(currentBiomeSimpleName))
                                {
                                    isBiomeValid = true;
                                    break;
                                }
                            }

                            if (!isBiomeValid)
                            {
                                continue;
                            }
                        }

                        if (data.isWater)
                        {
                            int chunkX = event.getChunkX() * 16;
                            int chunkZ = event.getChunkZ() * 16;

                            BlockPos pos = new BlockPos(chunkX, 62, chunkZ);
                            boolean isInWater = event.getWorld().getBlockState(pos).getMaterial().isLiquid();

                            if (!isInWater)
                            {
                                continue;
                            }
                        }

                        float minChance;
                        float maxChance;

                        switch (data.spawnChancePriority.toLowerCase())
                        {
                            case "low":
                                minChance = 0.01f;
                                maxChance = 0.25f;
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
                            EnumCreatureType creatureType = CodeGeneric.getCreatureType(_class);

                            Biome currentBiome = event.getWorld().getBiome(
                                    new BlockPos(event.getChunkX() * 16, 64, event.getChunkZ() * 16)
                            );

                            List<Biome.SpawnListEntry> spawnList = currentBiome.getSpawnableList(creatureType);

                            int currentEntitiesInChunk = (int) spawnList.stream()
                                .filter(entry -> entry.entityClass.equals(_class))
                                .count();

                            if (currentEntitiesInChunk < data.maxEntitiesPerChunk)
                            {
                                spawnList.add(new Biome.SpawnListEntry(
                                        (Class<? extends EntityLiving>) _class,
                                        data.weight,
                                        data.groupCountMin,
                                        data.groupCountMax
                                ));
                            }
                        }
                    }
                }
            }
        }
    }
}