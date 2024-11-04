package org.imesense.dynamicspawncontrol.core.worldcache;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.imesense.dynamicspawncontrol.core.LogFile;

import java.util.HashSet;

public class CacheEvent
{
    private static boolean instanceExists = false;

    private static CacheMonitor cacheMonitor = null;

    public CacheEvent()
    {
        //CodeGenericUtil.printInitClassToLog(this.getClass());

        if (instanceExists)
        {
            LogFile.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        cacheMonitor = new CacheMonitor();
    }

    @SubscribeEvent
    public synchronized void onWorldTick_0(TickEvent.WorldTickEvent worldTickEvent)
    {
        if (worldTickEvent.phase == TickEvent.Phase.END)
        {
            Cache.Instance.TickCounter++;

            if (Cache.Instance.TickCounter >= Cache.Instance._DYNAMIC_UPDATE_INTERVAL)
            {
                Cache.Instance.TickCounter = 0;

                Cache.Instance.copyActualToBuffer();
                Cache.Instance.updateCache(worldTickEvent.world);

                if (Cache.Instance.IsFirstUpdate)
                {
                    Cache.Instance._DYNAMIC_UPDATE_INTERVAL =
                            Cache.Instance.SUBSEQUENT_UPDATE_INTERVAL;

                    Cache.Instance.IsFirstUpdate = false;
                }
            }
        }
    }

    @SubscribeEvent
    public synchronized void onPlayerLoggedIn_1(PlayerEvent.PlayerLoggedInEvent playerLoggedInEvent)
    {
        if (!Cache.Instance.IsPrimaryPlayerLogged)
        {
            Cache.Instance.IsPrimaryPlayerLogged = true;
            Cache.Instance._DYNAMIC_UPDATE_INTERVAL = Cache.Instance.FIRST_UPDATE_INTERVAL;
            Cache.Instance.TickCounter = 0;
            Cache.Instance.IsFirstUpdate = true;
        }

        Cache.Instance.copyActualToBuffer();
    }

    @SubscribeEvent
    public synchronized void onPlayerLoggedOut_2(PlayerEvent.PlayerLoggedOutEvent playerLoggedOutEvent)
    {
        Cache.Instance.copyActualToBuffer();
    }

    @SubscribeEvent
    public synchronized void onRenderOverlay_3(RenderGameOverlayEvent.Post post)
    {
        //if (!DataGameDebugger.ConfigDataMonitor.Instance.getDebugMonitorCache())
        //{
        //    return;
        //}

        if (post.getType() == RenderGameOverlayEvent.ElementType.TEXT)
        {
            cacheMonitor.renderDebugInfo(post.getWindow().getGuiScaledWidth(), post.getWindow().getGuiScaledHeight());
        }
    }

    @SubscribeEvent
    public synchronized void onEntityJoinWorld_4(EntityJoinWorldEvent entityJoinWorldEvent)
    {
        World world = entityJoinWorldEvent.getWorld();
        Entity entity = entityJoinWorldEvent.getEntity();

        if (world.isClientSide() || !(world instanceof ServerWorld))
        {
            return;
        }

        ServerWorld serverWorld = (ServerWorld) world;

        Cache.Instance.updateCache(serverWorld);

        ChunkPos entityChunkPos = new ChunkPos(entity.blockPosition());

        if (Cache.Instance.CACHE_VALID_CHUNKS.contains(entityChunkPos))
        {
            if (entity instanceof MobEntity)
            {
                if (entity instanceof AnimalEntity)
                {
                    Cache.Instance.CACHED_ACTUAL_ANIMALS.add((AnimalEntity) entity);
                }
                else if (entity instanceof MobEntity)
                {
                    Cache.Instance.CACHED_ACTUAL_HOSTILES.add((MobEntity) entity);
                }
            }

            if (entity instanceof LivingEntity)
            {
                String entityName = entity.getName().getString();

                Cache.Instance.CACHED_ACTUAL_ALL.add((LivingEntity) entity);

                Cache.Instance.ENTITIES_ACTUAL_BY_NAME.computeIfAbsent(entityName, k ->
                        new HashSet<>()).add((LivingEntity) entity);

                ResourceLocation resourceLocation = EntityType.getKey(entity.getType());

                if (resourceLocation != null)
                {
                    Cache.Instance.ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.computeIfAbsent(resourceLocation, k ->
                            new HashSet<>()).add((LivingEntity) entity);
                }
            }
        }
    }

    @SubscribeEvent
    public synchronized void updateEntitySpawnEvent_5(LivingSpawnEvent.CheckSpawn event)
    {
        Entity entity = event.getEntity();

        ResourceLocation entityKey = EntityType.getKey(entity.getType());

        CacheStorage.EntityData entityData = CacheStorage.Instance.getEntityDataByResourceLocation(entityKey);

        if (entityData != null)
        {
            assert entityKey != null;

            int maxCount = entityData.getMaxCount();
            int currentCount = Cache.Instance.getEntitiesByResourceLocation(entityKey).size();

            if (currentCount > maxCount)
            {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}

