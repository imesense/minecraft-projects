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
     * @param event
     */
    @SubscribeEvent
    public synchronized void onWorldTick_0(TickEvent.WorldTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            Cache.instance.TickCounter++;

            if (Cache.instance.TickCounter >= Cache.instance.DynamicUpdateInterval)
            {
                Cache.instance.TickCounter = 0;

                Cache.instance.copyActualToBuffer();
                Cache.instance.updateCache(event.world);

                if (Cache.instance.isFirstUpdate)
                {
                    Cache.instance.DynamicUpdateInterval = Cache.instance.SUBSEQUENT_UPDATE_INTERVAL;
                    Cache.instance.isFirstUpdate = false;
                }
            }
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onPlayerLoggedIn_1(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!Cache.instance.isPrimaryPlayerLogged)
        {
            Cache.instance.isPrimaryPlayerLogged = true;
            Cache.instance.DynamicUpdateInterval = Cache.instance.FIRST_UPDATE_INTERVAL;
            Cache.instance.TickCounter = 0;
            Cache.instance.isFirstUpdate = true;
        }

        Cache.instance.copyActualToBuffer();
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onPlayerLoggedOut_2(PlayerEvent.PlayerLoggedOutEvent event)
    {
        Cache.instance.copyActualToBuffer();
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onRenderOverlay_3(RenderGameOverlayEvent.Post event)
    {
        if (!DataGameDebugger.ConfigDataMonitor.instance.getDebugMonitorCache())
        {
            return;
        }

        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT)
        {
            cacheMonitor.renderDebugInfo(event.getResolution());
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onEntityJoinWorld_4(EntityJoinWorldEvent event)
    {
        World world = event.getWorld();
        Entity entity = event.getEntity();

        if (world.isRemote || !(world instanceof WorldServer))
        {
            return;
        }

        WorldServer worldServer = (WorldServer) world;

        Cache.instance.updateCache(worldServer);

        if (Cache.instance.CACHE_VALID_CHUNKS.contains(new ChunkPos(entity.chunkCoordX, entity.chunkCoordZ)))
        {
            if (entity instanceof IAnimals)
            {
                if (entity instanceof EntityAnimal)
                {
                    Cache.instance.CACHED_ACTUAL_ANIMALS.add((EntityAnimal) entity);
                }
                else if (entity instanceof EntityMob)
                {
                    Cache.instance.CACHED_ACTUAL_HOSTILES.add((IAnimals) entity);
                }
            }

            if (entity instanceof EntityLivingBase)
            {
                String entityName = entity.getName();

                Cache.instance.CACHED_ACTUAL_ALL.add((EntityLivingBase) entity);

                Cache.instance.ENTITIES_ACTUAL_BY_NAME.computeIfAbsent(entityName, k ->
                        new HashSet<>()).add((EntityLivingBase) entity);

                ResourceLocation entityKey = EntityList.getKey(entity);

                if (entityKey != null)
                {
                    Cache.instance.ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.computeIfAbsent(entityKey, k ->
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

        CacheStorage.EntityData entityData = CacheStorage.instance.getEntityDataByResourceLocation(entityKey);

        if (entityData != null)
        {
            assert entityKey != null;

            int maxCount = entityData.getMaxCount();
            int currentCount = Cache.instance.getEntitiesByResourceLocation(entityKey).size();

            if (currentCount > maxCount)
            {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
