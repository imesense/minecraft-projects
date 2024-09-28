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
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.configs.CfgGameDebugger;
import org.imesense.dynamicspawncontrol.technical.configs.ConfigGameDebugger;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.util.HashSet;

@Mod.EventBusSubscriber
public final class CacheEvents
{
    static CacheMonitor cacheMonitor = null;

    private static boolean instanceExists = false;

    public CacheEvents()
    {
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        cacheMonitor = new CacheMonitor();

        CodeGenericUtils.printInitClassToLog(CacheEvents.class);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public synchronized void onWorldTick_0(TickEvent.WorldTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            Cache.getInstance().TickCounter++;

            if (Cache.getInstance().TickCounter >= Cache.getInstance().DynamicUpdateInterval)
            {
                Cache.getInstance().TickCounter = 0;

                Cache.getInstance().copyActualToBuffer();
                Cache.getInstance().updateCache(event.world);

                if (Cache.getInstance().isFirstUpdate)
                {
                    Cache.getInstance().DynamicUpdateInterval = Cache.getInstance().SUBSEQUENT_UPDATE_INTERVAL;
                    Cache.getInstance().isFirstUpdate = false;
                }
            }
        }
    }

    @SubscribeEvent
    public synchronized void onPlayerLoggedIn_1(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!Cache.getInstance().isPrimaryPlayerLogged)
        {
            Cache.getInstance().isPrimaryPlayerLogged = true;
            Cache.getInstance().DynamicUpdateInterval = Cache.getInstance().FIRST_UPDATE_INTERVAL;
            Cache.getInstance().TickCounter = 0;
            Cache.getInstance().isFirstUpdate = true;
        }


        Cache.getInstance().copyActualToBuffer();
    }

    @SubscribeEvent
    public synchronized void onPlayerLoggedOut_2(PlayerEvent.PlayerLoggedOutEvent event)
    {
        Cache.getInstance().copyActualToBuffer();
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public synchronized void onRenderOverlay_3(RenderGameOverlayEvent.Post event)
    {
        if (!CfgGameDebugger.instance.getMonitor().getDebugMonitorOpt())
        {

        }
        //if (!ConfigGameDebugger.DebugMonitorCache)
        //{
        //    return;
        //}

        //if (!CfgGameDebugger.instance.isMonitorDebugEnabled())
       // {
        //    return;
        //}

        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT)
        {
            cacheMonitor.renderDebugInfo(event.getResolution());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public synchronized void onEntityJoinWorld_4(EntityJoinWorldEvent event)
    {
        World world = event.getWorld();
        Entity entity = event.getEntity();

        if (world.isRemote || !(world instanceof WorldServer))
        {
            return;
        }

        WorldServer worldServer = (WorldServer) world;

        Cache.getInstance().updateCache(worldServer);

        if (Cache.getInstance().CACHE_VALID_CHUNKS.contains(new ChunkPos(entity.chunkCoordX, entity.chunkCoordZ)))
        {
            if (entity instanceof IAnimals)
            {
                if (entity instanceof EntityAnimal)
                {
                    Cache.getInstance().CACHED_ACTUAL_ANIMALS.add((EntityAnimal) entity);
                }
                else if (entity instanceof EntityMob)
                {
                    Cache.getInstance().CACHED_ACTUAL_HOSTILES.add((IAnimals) entity);
                }
            }

            if (entity instanceof EntityLivingBase)
            {
                String entityName = entity.getName();

                Cache.getInstance().CACHED_ACTUAL_ALL.add((EntityLivingBase) entity);

                Cache.getInstance().ENTITIES_ACTUAL_BY_NAME.computeIfAbsent(entityName, k ->
                        new HashSet<>()).add((EntityLivingBase) entity);

                ResourceLocation entityKey = EntityList.getKey(entity);

                if (entityKey != null)
                {
                    Cache.getInstance().ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.computeIfAbsent(entityKey, k ->
                            new HashSet<>()).add((EntityLivingBase) entity);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public synchronized void updateEntitySpawnEvent_5(LivingSpawnEvent.CheckSpawn event)
    {
        Entity entity = event.getEntity();

        ResourceLocation entityKey = EntityList.getKey(entity);

        CacheStorage.EntityData entityData = CacheStorage.getInstance().getEntityDataByResourceLocation(entityKey);

        if (entityData != null)
        {
            assert entityKey != null;

            int maxCount = entityData.getMaxCount();
            int currentCount = Cache.getInstance().getEntitiesByResourceLocation(entityKey).size();

            if (currentCount > maxCount)
            {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
