package org.imesense.dynamicspawncontrol.core.worldcache;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.imesense.dynamicspawncontrol.core.LogFile;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Cache
{
    public static Cache Instance;

    public int TickCounter = 0;

    public final int FIRST_UPDATE_INTERVAL = 1200;

    private static boolean instanceExists = false;

    public volatile int _DYNAMIC_UPDATE_INTERVAL = 1200;

    public final int SUBSEQUENT_UPDATE_INTERVAL = 4800;

    public boolean IsFirstUpdate = true;

    public boolean IsPrimaryPlayerLogged = false;

    public final Set<ChunkPos> CACHE_VALID_CHUNKS = new HashSet<>();

    public final Set<AnimalEntity> CACHED_ACTUAL_ANIMALS = new HashSet<>();

    public final Set<AnimalEntity> CACHED_BUFFER_ANIMALS = new HashSet<>();

    public final Set<MobEntity> CACHED_ACTUAL_HOSTILES = new HashSet<>();

    public final Set<MobEntity> CACHED_BUFFER_HOSTILES = new HashSet<>();

    public final Set<LivingEntity> CACHED_ACTUAL_ALL = new HashSet<>();

    public final Set<LivingEntity> CACHED_BUFFER_ALL = new HashSet<>();

    public final ConcurrentMap<String, Set<LivingEntity>> ENTITIES_ACTUAL_BY_NAME = new ConcurrentHashMap<>();

    public final ConcurrentMap<String, Set<LivingEntity>> ENTITIES_BUFFER_BY_NAME = new ConcurrentHashMap<>();

    public final ConcurrentMap<ResourceLocation, Set<LivingEntity>> ENTITIES_ACTUAL_BY_RESOURCE_LOCATION = new ConcurrentHashMap<>();

    public final ConcurrentMap<ResourceLocation, Set<LivingEntity>> ENTITIES_BUFFER_BY_RESOURCE_LOCATION = new ConcurrentHashMap<>();

    public Cache()
    {
        //CodeGenericUtil.printInitClassToLog(this.getClass());

        if (instanceExists)
        {
            LogFile.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        Instance = this;
    }

    public void updateCache(@Nonnull World world) {
        cleanActualCache();

        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;

            for (ServerPlayerEntity playerEntity : serverWorld.getPlayers((player) -> true)) {
                Set<ChunkPos> validChunks = totalValidChunksSpawnForPlayer(serverWorld, playerEntity);
                CACHE_VALID_CHUNKS.addAll(validChunks);
            }
        }

        for (Entity entity : world.getEntities()) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;

                if (CACHE_VALID_CHUNKS.contains(new ChunkPos(entity.blockPosition()))) {
                    if (entity instanceof MobEntity) {
                        if (entity instanceof AnimalEntity) {
                            CACHED_ACTUAL_ANIMALS.add((AnimalEntity) entity);
                        } else if (entity instanceof MobEntity) {
                            CACHED_ACTUAL_HOSTILES.add((MobEntity) entity);
                        }
                    }

                    CACHED_ACTUAL_ALL.add(livingEntity);

                    String entityName = livingEntity.getName().getString();

                    ENTITIES_ACTUAL_BY_NAME.computeIfAbsent(entityName, k -> new HashSet<>()).add(livingEntity);

                    ResourceLocation resourceLocation = EntityType.getKey(entity.getType());

                    if (resourceLocation != null) {
                        ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.computeIfAbsent(resourceLocation, k -> new HashSet<>()).add(livingEntity);
                    }
                }
            }
        }
    }

    private Set<ChunkPos> totalValidChunksSpawnForPlayer(ServerWorld serverWorld, ServerPlayerEntity playerEntity)
    {
        Set<ChunkPos> validChunks = new HashSet<>();

        int viewDistance =
                Objects.requireNonNull(serverWorld.getServer()).getPlayerList().getViewDistance();

        int playerChunkX = MathHelper.floor(playerEntity.getX()) >> 4;
        int playerChunkZ = MathHelper.floor(playerEntity.getZ()) >> 4;

        for (int x = playerChunkX - viewDistance; x <= playerChunkX + viewDistance; x++)
        {
            for (int z = playerChunkZ - viewDistance; z <= playerChunkZ + viewDistance; z++)
            {
                ChunkPos chunkPos = new ChunkPos(x, z);

                if (serverWorld.getChunkSource().hasChunk(x, z))
                {
                    validChunks.add(chunkPos);
                }
            }
        }

        return validChunks;
    }

    public int getActualAnimalCount()
    {
        return CACHED_ACTUAL_ANIMALS.size();
    }

    public int getActualTotalEntityCount()
    {
        return CACHED_ACTUAL_ALL.size();
    }

    public int getActualHostileEntityCount()
    {
        return CACHED_ACTUAL_HOSTILES.size();
    }

    public int getBufferAnimalCount()
    {
        return CACHED_BUFFER_ANIMALS.size();
    }

    public int getBufferTotalEntityCount()
    {
        return CACHED_BUFFER_ALL.size();
    }

    public int getBufferHostileEntityCount()
    {
        return CACHED_BUFFER_HOSTILES.size();
    }

    public int getValidChunkCount()
    {
        return CACHE_VALID_CHUNKS.size();
    }

    @Nonnull
    public Set<LivingEntity> getEntitiesByResourceLocation(@Nonnull ResourceLocation resourceLocation)
    {
        return ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.getOrDefault(resourceLocation, Collections.emptySet());
    }

    public void copyActualToBuffer()
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

    public void cleanActualCache()
    {
        CACHED_ACTUAL_ANIMALS.clear();
        CACHED_ACTUAL_HOSTILES.clear();
        CACHED_ACTUAL_ALL.clear();
        ENTITIES_ACTUAL_BY_NAME.clear();
        ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.clear();

        CACHE_VALID_CHUNKS.clear();
    }

    public void cleanBufferCache()
    {
        CACHED_BUFFER_ANIMALS.clear();
        CACHED_BUFFER_HOSTILES.clear();
        CACHED_BUFFER_ALL.clear();
        ENTITIES_BUFFER_BY_NAME.clear();
        ENTITIES_BUFFER_BY_RESOURCE_LOCATION.clear();
    }
}
