package org.imesense.dynamicspawncontrol.core.worldcache;

import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@InitLog
public final class CacheGeneralStorage
{
    private static volatile CacheGeneralStorage _INSTANCE;

    public static CacheGeneralStorage getInstance()
    {
        return CodeGeneric.getInstance(CacheGeneralStorage.class);
    }

    public int TickCounter = 0;

    public final int FIRST_UPDATE_INTERVAL = 1200;

    public volatile int _DYNAMIC_UPDATE_INTERVAL = 1200;

    public final int SUBSEQUENT_UPDATE_INTERVAL = 2400;

    public boolean IsFirstUpdate = true;

    public boolean IsPrimaryPlayerLogged = false;

    @Getter
    private long lastUpdateTime = System.currentTimeMillis();

    public final Set<ChunkPos> CACHE_VALID_CHUNKS = new HashSet<>();

    public final Set<EntityAnimal> CACHED_ACTUAL_ANIMALS = new HashSet<>();

    public final Set<EntityAnimal> CACHED_BUFFER_ANIMALS = new HashSet<>();

    public final Set<IAnimals> CACHED_ACTUAL_HOSTILES = new HashSet<>();

    public final Set<IAnimals> CACHED_BUFFER_HOSTILES = new HashSet<>();

    public final Set<EntityLivingBase> CACHED_ACTUAL_WATER_MOBS = new HashSet<>();

    public final Set<EntityLivingBase> CACHED_BUFFER_WATER_MOBS = new HashSet<>();

    public final Set<EntityLivingBase> CACHED_ACTUAL_ALL = new HashSet<>();

    public final Set<EntityLivingBase> CACHED_BUFFER_ALL = new HashSet<>();

    public final ConcurrentMap<String, Set<EntityLivingBase>> ENTITIES_ACTUAL_BY_NAME = new ConcurrentHashMap<>();

    public final ConcurrentMap<String, Set<EntityLivingBase>> ENTITIES_BUFFER_BY_NAME = new ConcurrentHashMap<>();

    public final ConcurrentMap<ResourceLocation, Set<EntityLivingBase>> ENTITIES_ACTUAL_BY_RESOURCE_LOCATION = new ConcurrentHashMap<>();

    public final ConcurrentMap<ResourceLocation, Set<EntityLivingBase>> ENTITIES_BUFFER_BY_RESOURCE_LOCATION = new ConcurrentHashMap<>();

    public CacheGeneralStorage()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void updateCache(@Nonnull World world)
    {
        this.lastUpdateTime = System.currentTimeMillis();

        cleanActualCache();

        if (world instanceof WorldServer)
        {
            WorldServer worldServer = (WorldServer) world;

            for (EntityPlayer entityPlayer : world.playerEntities)
            {
                Set<ChunkPos> validChunks =
                        CacheFunctional.getInstance().totalValidChunksSpawnForPlayer(worldServer, (EntityPlayerMP) entityPlayer);

                CACHE_VALID_CHUNKS.addAll(validChunks);
            }
        }

        for (Entity entity : world.loadedEntityList)
        {
            if (entity instanceof EntityLivingBase)
            {
                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;

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
                        else if (entity instanceof EntityWaterMob)
                        {
                            CACHED_ACTUAL_WATER_MOBS.add((EntityWaterMob) entity);
                        }
                    }

                    CACHED_ACTUAL_ALL.add(entityLivingBase);

                    String entityName = entity.getName();

                    ENTITIES_ACTUAL_BY_NAME.computeIfAbsent(entityName, k ->
                            new HashSet<>()).add(entityLivingBase);

                    ResourceLocation resourceLocation = EntityList.getKey(entity);

                    if (resourceLocation != null)
                    {
                        ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.computeIfAbsent(resourceLocation, k ->
                                new HashSet<>()).add(entityLivingBase);
                    }
                }
            }
        }
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

    public int getActualWaterMobCount() { return CACHED_ACTUAL_WATER_MOBS.size(); }

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

    public int getBufferWaterMobCount() { return CACHED_BUFFER_WATER_MOBS.size(); }

    public int getValidChunkCount()
    {
        return CACHE_VALID_CHUNKS.size();
    }

    @Nonnull
    public Set<EntityLivingBase> getEntitiesByResourceLocation(@Nonnull ResourceLocation resourceLocation)
    {
        return ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.getOrDefault(resourceLocation, Collections.emptySet());
    }

    public void copyActualToBuffer()
    {
        CACHED_BUFFER_ANIMALS.clear();

        CACHED_BUFFER_ANIMALS.addAll(CACHED_ACTUAL_ANIMALS);

        CACHED_BUFFER_HOSTILES.clear();

        CACHED_BUFFER_HOSTILES.addAll(CACHED_ACTUAL_HOSTILES);

        CACHED_BUFFER_WATER_MOBS.clear();

        CACHED_BUFFER_WATER_MOBS.addAll(CACHED_ACTUAL_WATER_MOBS);

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
        CACHED_ACTUAL_WATER_MOBS.clear();
        CACHED_ACTUAL_ALL.clear();
        ENTITIES_ACTUAL_BY_NAME.clear();
        ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.clear();

        CACHE_VALID_CHUNKS.clear();
    }

    public void cleanBufferCache()
    {
        CACHED_BUFFER_ANIMALS.clear();
        CACHED_BUFFER_HOSTILES.clear();
        CACHED_BUFFER_WATER_MOBS.clear();
        CACHED_BUFFER_ALL.clear();
        ENTITIES_BUFFER_BY_NAME.clear();
        ENTITIES_BUFFER_BY_RESOURCE_LOCATION.clear();
    }
}
