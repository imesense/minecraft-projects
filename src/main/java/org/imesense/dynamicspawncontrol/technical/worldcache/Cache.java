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
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class Cache
{
    /**
     *
     */
    public static int TickCounter = 0;

    /**
     *
     */
    public static final int UPDATE_INTERVAL = 1200;

    /**
     *
     */
    public static final Set<ChunkPos> CachedValidChunks = new HashSet<>();

    /**
     *
     */
    public static final Set<EntityAnimal> CachedAnimals = new HashSet<>();

    /**
     *
     */
    public static final Set<IAnimals> CachedHostileEntities = new HashSet<>();

    /**
     *
     */
    public static final Set<EntityLivingBase> CachedAllEntities = new HashSet<>();

    /**
     *
     */
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     *
     */
    public static final ConcurrentMap<String, Set<EntityLivingBase>> EntitiesByName = new ConcurrentHashMap<>();

    /**
     *
     */
    public static final ConcurrentMap<ResourceLocation, Set<EntityLivingBase>> EntitiesByResourceLocation = new ConcurrentHashMap<>();

    /**
     *
     * @param world
     */
    public static void updateCacheAsync(@Nonnull World world)
    {
        executor.submit(() -> updateCache(world));
    }

    /**
     *
     * @param world
     */
    public static void updateCache(@Nonnull World world)
    {
        CachedAnimals.clear();
        CachedHostileEntities.clear();
        CachedAllEntities.clear();
        EntitiesByName.clear();
        EntitiesByResourceLocation.clear();
        CachedValidChunks.clear();

        if (world instanceof WorldServer)
        {
            WorldServer worldServer = (WorldServer) world;
            Set<ChunkPos> validChunks = totalValidChunksSpawn(worldServer);

            CachedValidChunks.addAll(validChunks);
        }

        for (Entity entity : world.loadedEntityList)
        {
            if (entity instanceof EntityLivingBase)
            {
                EntityLivingBase livingEntity = (EntityLivingBase) entity;

                if (CachedValidChunks.contains(new ChunkPos(entity.chunkCoordX, entity.chunkCoordZ)))
                {
                    if (entity instanceof IAnimals)
                    {
                        if (entity instanceof EntityAnimal)
                        {
                            CachedAnimals.add((EntityAnimal) entity);
                        }
                        else if (entity instanceof EntityMob)
                        {
                            CachedHostileEntities.add((IAnimals) entity);
                        }
                    }

                    CachedAllEntities.add(livingEntity);

                    String entityName = entity.getName();
                    EntitiesByName.computeIfAbsent(entityName, k -> new HashSet<>()).add(livingEntity);

                    ResourceLocation entityKey = EntityList.getKey(entity);

                    if (entityKey != null)
                    {
                        EntitiesByResourceLocation.computeIfAbsent(entityKey, k -> new HashSet<>()).add(livingEntity);
                    }
                }
            }
        }
    }

    /**
     *
     * @param worldServer
     * @return
     */
    private static Set<ChunkPos> totalValidChunksSpawn(@Nonnull WorldServer worldServer)
    {
        Set<ChunkPos> setPos = new HashSet<>();

        for (EntityPlayer player : worldServer.playerEntities)
        {
            if (!player.isSpectator())
            {
                int x = MathHelper.floor(player.posX / 16.0f);
                int z = MathHelper.floor(player.posZ / 16.0f);

                for (int dx = -8; dx <= 8; ++dx)
                {
                    for (int dz = -8; dz <= 8; ++dz)
                    {
                        boolean correct = (dx == -8 || dx == 8 || dz == -8 || dz == 8);
                        ChunkPos pos = new ChunkPos(dx + x, dz + z);

                        if (!setPos.contains(pos))
                        {
                            if (!correct && worldServer.getWorldBorder().contains(pos))
                            {
                                PlayerChunkMapEntry playerServer = worldServer.getPlayerChunkMap().getEntry(pos.x, pos.z);

                                if (playerServer != null && playerServer.isSentToPlayers())
                                {
                                    setPos.add(pos);
                                }
                            }
                        }
                    }
                }
            }
        }

        return setPos;
    }

    /**
     *
     * @return
     */
    public static int getAnimalCount()
    {
        return CachedAnimals.size();
    }

    /**
     *
     * @return
     */
    public static int getTotalEntityCount()
    {
        return CachedAllEntities.size();
    }

    /**
     *
     * @return
     */
    public static int getHostileEntityCount()
    {
        return CachedHostileEntities.size();
    }

    /**
     *
     * @param resourceLocation
     * @return
     */
    @Nonnull
    public static Set<EntityLivingBase> getEntitiesByResourceLocation(@Nonnull ResourceLocation resourceLocation)
    {
        return EntitiesByResourceLocation.getOrDefault(resourceLocation, Collections.emptySet());
    }
}
