package org.imesense.dynamicspawncontrol.technical.worldcache;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class Cache
{
    public static int TickCounter = 0;
    public static final int UPDATE_INTERVAL = 1200;
    public static final Set<ChunkPos> CACHE_VALID_CHUNKS = new HashSet<>();
    public static final Set<EntityAnimal> CACHED_ACTUAL_ANIMALS = new HashSet<>();
    public static final Set<EntityAnimal> CACHED_BUFFER_ANIMALS = new HashSet<>();
    public static final Set<IAnimals> CACHED_ACTUAL_HOSTILES = new HashSet<>();
    public static final Set<IAnimals> CACHED_BUFFER_HOSTILES = new HashSet<>();
    public static final Set<EntityLivingBase> CACHED_ACTUAL_ALL = new HashSet<>();
    public static final Set<EntityLivingBase> CACHED_BUFFER_ALL = new HashSet<>();
    public static final ConcurrentMap<String, Set<EntityLivingBase>> ENTITIES_ACTUAL_BY_NAME = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, Set<EntityLivingBase>> ENTITIES_BUFFER_BY_NAME = new ConcurrentHashMap<>();
    public static final ConcurrentMap<ResourceLocation, Set<EntityLivingBase>> ENTITIES_ACTUAL_BY_RESOURCE_LOCATION = new ConcurrentHashMap<>();
    public static final ConcurrentMap<ResourceLocation, Set<EntityLivingBase>> ENTITIES_BUFFER_BY_RESOURCE_LOCATION = new ConcurrentHashMap<>();

    public static void updateCache(@Nonnull World world)
    {
        cleanActualCache();

        if (world instanceof WorldServer)
        {
            WorldServer worldServer = (WorldServer) world;

            for (EntityPlayer player : world.playerEntities)
            {
                Set<ChunkPos> validChunks = totalValidChunksSpawnForPlayer(worldServer, player);
                CACHE_VALID_CHUNKS.addAll(validChunks);
            }
        }

        for (Entity entity : world.loadedEntityList)
        {
            if (entity instanceof EntityLivingBase)
            {
                EntityLivingBase livingEntity = (EntityLivingBase) entity;

                if (CACHE_VALID_CHUNKS.contains(new ChunkPos(entity.chunkCoordX, entity.chunkCoordZ)))
                {
                    if (entity instanceof IAnimals)
                    {
                        if (entity instanceof EntityAnimal)
                        {
                            CACHED_ACTUAL_ANIMALS.add((EntityAnimal) entity);
                        }
                        else if (entity instanceof EntityMob)
                        {
                            CACHED_ACTUAL_HOSTILES.add((IAnimals) entity);
                        }
                    }

                    CACHED_ACTUAL_ALL.add(livingEntity);

                    String entityName = entity.getName();

                    ENTITIES_ACTUAL_BY_NAME.computeIfAbsent(entityName, k ->
                            new HashSet<>()).add(livingEntity);

                    ResourceLocation entityKey = EntityList.getKey(entity);
                    if (entityKey != null)
                    {
                        ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.computeIfAbsent(entityKey, k ->
                                new HashSet<>()).add(livingEntity);
                    }
                }
            }
        }
    }

    private static Set<ChunkPos> totalValidChunksSpawnForPlayer(WorldServer worldServer, EntityPlayer player)
    {
        Set<ChunkPos> validChunks = new HashSet<>();

        int viewDistance = Objects.requireNonNull(worldServer.getMinecraftServer()).getPlayerList().getViewDistance();
        int playerChunkX = MathHelper.floor(player.posX) >> 4;
        int playerChunkZ = MathHelper.floor(player.posZ) >> 4;

        for (int x = playerChunkX - viewDistance; x <= playerChunkX + viewDistance; x++)
        {
            for (int z = playerChunkZ - viewDistance; z <= playerChunkZ + viewDistance; z++)
            {
                ChunkPos chunkPos = new ChunkPos(x, z);

                if (worldServer.getChunkProvider().isChunkGeneratedAt(x, z))
                {
                    validChunks.add(chunkPos);
                }
            }
        }

        return validChunks;
    }

    public static int getActualAnimalCount()
    {
        return CACHED_ACTUAL_ANIMALS.size();
    }

    public static int getActualTotalEntityCount()
    {
        return CACHED_ACTUAL_ALL.size();
    }

    public static int getActualHostileEntityCount()
    {
        return CACHED_ACTUAL_HOSTILES.size();
    }

    public static int getBufferAnimalCount()
    {
        return CACHED_BUFFER_ANIMALS.size();
    }

    public static int getBufferTotalEntityCount()
    {
        return CACHED_BUFFER_ALL.size();
    }

    public static int getBufferHostileEntityCount()
    {
        return CACHED_BUFFER_HOSTILES.size();
    }

    public static int getValidChunkCount()
    {
        return CACHE_VALID_CHUNKS.size();
    }

    @Nonnull
    public static Set<EntityLivingBase> getEntitiesByResourceLocation(@Nonnull ResourceLocation resourceLocation)
    {
        return ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.getOrDefault(resourceLocation, Collections.emptySet());
    }

    public static void copyActualToBuffer()
    {
        CACHED_BUFFER_ANIMALS.clear();

        CACHED_BUFFER_ANIMALS.addAll(CACHED_ACTUAL_ANIMALS);

        CACHED_BUFFER_HOSTILES.clear();

        CACHED_BUFFER_HOSTILES.addAll(CACHED_ACTUAL_HOSTILES);

        CACHED_BUFFER_ALL.clear();

        CACHED_BUFFER_ALL.addAll(CACHED_ACTUAL_ALL);

        ENTITIES_BUFFER_BY_NAME.clear();

        ENTITIES_ACTUAL_BY_NAME.forEach((name, set) ->
                ENTITIES_BUFFER_BY_NAME.put(name, new HashSet<>(set)));

        ENTITIES_BUFFER_BY_RESOURCE_LOCATION.clear();

        ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.forEach((key, set) ->
                ENTITIES_BUFFER_BY_RESOURCE_LOCATION.put(key, new HashSet<>(set)));
    }

    public static void cleanActualCache()
    {
        CACHED_ACTUAL_ANIMALS.clear();
        CACHED_ACTUAL_HOSTILES.clear();
        CACHED_ACTUAL_ALL.clear();
        ENTITIES_ACTUAL_BY_NAME.clear();
        ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.clear();

        CACHE_VALID_CHUNKS.clear();
    }

    public static void cleanBufferCache()
    {
        CACHED_BUFFER_ANIMALS.clear();
        CACHED_BUFFER_HOSTILES.clear();
        CACHED_BUFFER_ALL.clear();
        ENTITIES_BUFFER_BY_NAME.clear();
        ENTITIES_BUFFER_BY_RESOURCE_LOCATION.clear();
    }
}
