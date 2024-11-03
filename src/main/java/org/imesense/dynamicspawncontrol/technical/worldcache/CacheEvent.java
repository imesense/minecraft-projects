package org.imesense.dynamicspawncontrol.technical.worldcache;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.config.gamedebugger.DataGameDebugger;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.util.HashSet;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class CacheEvent
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    private static CacheMonitor cacheMonitor = null;

    /**
     *
     */
    public CacheEvent()
    {
		CodeGenericUtil.printInitClassToLog(this.getClass());
		
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        cacheMonitor = new CacheMonitor();
    }

    /**
     *
     * @param worldTickEvent
     */
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

    /**
     *
     * @param playerLoggedInEvent
     */
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

    /**
     *
     * @param playerLoggedOutEvent
     */
    @SubscribeEvent
    public synchronized void onPlayerLoggedOut_2(PlayerEvent.PlayerLoggedOutEvent playerLoggedOutEvent)
    {
        Cache.Instance.copyActualToBuffer();
    }

    /**
     *
     * @param post
     */
    @SubscribeEvent
    public synchronized void onRenderOverlay_3(RenderGameOverlayEvent.Post post)
    {
        if (!DataGameDebugger.ConfigDataMonitor.Instance.getDebugMonitorCache())
        {
            return;
        }

        if (post.getType() == RenderGameOverlayEvent.ElementType.TEXT)
        {
            cacheMonitor.renderDebugInfo(post.getResolution());
        }
    }

    /**
     *
     * @param entityJoinWorldEvent
     */
    @SubscribeEvent
    public synchronized void onEntityJoinWorld_4(EntityJoinWorldEvent entityJoinWorldEvent)
    {
        World world = entityJoinWorldEvent.getWorld();
        Entity entity = entityJoinWorldEvent.getEntity();

        if (world.isRemote || !(world instanceof WorldServer))
        {
            return;
        }

        WorldServer worldServer = (WorldServer) world;

        Cache.Instance.updateCache(worldServer);

        if (Cache.Instance.CACHE_VALID_CHUNKS.contains(new ChunkPos(entity.chunkCoordX, entity.chunkCoordZ)))
        {
            if (entity instanceof IAnimals)
            {
                if (entity instanceof EntityAnimal)
                {
                    Cache.Instance.CACHED_ACTUAL_ANIMALS.add((EntityAnimal) entity);
                }
                else if (entity instanceof EntityMob)
                {
                    Cache.Instance.CACHED_ACTUAL_HOSTILES.add((IAnimals) entity);
                }
            }

            if (entity instanceof EntityLivingBase)
            {
                String entityName = entity.getName();

                Cache.Instance.CACHED_ACTUAL_ALL.add((EntityLivingBase) entity);

                Cache.Instance.ENTITIES_ACTUAL_BY_NAME.computeIfAbsent(entityName, k ->
                        new HashSet<>()).add((EntityLivingBase) entity);

                ResourceLocation resourceLocation = EntityList.getKey(entity);

                if (resourceLocation != null)
                {
                    Cache.Instance.ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.computeIfAbsent(resourceLocation, k ->
                            new HashSet<>()).add((EntityLivingBase) entity);
                }
            }
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void updateEntitySpawnEvent_5(LivingSpawnEvent.CheckSpawn event)
    {
        Entity entity = event.getEntity();

        ResourceLocation entityKey = EntityList.getKey(entity);

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
