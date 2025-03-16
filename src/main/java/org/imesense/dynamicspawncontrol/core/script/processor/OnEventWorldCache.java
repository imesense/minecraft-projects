package org.imesense.dynamicspawncontrol.core.script.processor;

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
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheGeneralStorage;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheEntityStorage;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheMonitorDebug;

import java.util.HashSet;

public final class OnEventWorldCache
{
    private static volatile OnEventWorldCache _INSTANCE;

    public static OnEventWorldCache getInstance()
    {
        return CodeGeneric.getInstance(OnEventWorldCache.class);
    }

    private static CacheMonitorDebug cacheMonitor = null;

    private final CacheEntityStorage CACHE_ENTITY_STORAGE = CacheEntityStorage.getInstance();

    private final CacheGeneralStorage CACHE_GENERAL_STORAGE = CacheGeneralStorage.getInstance();

    public OnEventWorldCache()
    {
		CodeGeneric.printInitClassToLog(this.getClass());

        cacheMonitor = new CacheMonitorDebug();
    }

    public void handleWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            this.CACHE_GENERAL_STORAGE.TickCounter++;

            if (this.CACHE_GENERAL_STORAGE.TickCounter >= this.CACHE_GENERAL_STORAGE._DYNAMIC_UPDATE_INTERVAL)
            {
                this.CACHE_GENERAL_STORAGE.TickCounter = 0;

                this.CACHE_GENERAL_STORAGE.copyActualToBuffer();
                this.CACHE_GENERAL_STORAGE.updateCache(event.world);

                if (this.CACHE_GENERAL_STORAGE.IsFirstUpdate)
                {
                    this.CACHE_GENERAL_STORAGE._DYNAMIC_UPDATE_INTERVAL =
                            this.CACHE_GENERAL_STORAGE.SUBSEQUENT_UPDATE_INTERVAL;

                    this.CACHE_GENERAL_STORAGE.IsFirstUpdate = false;
                }
            }
        }
    }

    public void handlePlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!this.CACHE_GENERAL_STORAGE.IsPrimaryPlayerLogged)
        {
            this.CACHE_GENERAL_STORAGE.IsPrimaryPlayerLogged = true;
            this.CACHE_GENERAL_STORAGE._DYNAMIC_UPDATE_INTERVAL = this.CACHE_GENERAL_STORAGE.FIRST_UPDATE_INTERVAL;
            this.CACHE_GENERAL_STORAGE.TickCounter = 0;
            this.CACHE_GENERAL_STORAGE.IsFirstUpdate = true;
        }

        this.CACHE_GENERAL_STORAGE.copyActualToBuffer();
    }

    public void handlePlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
    {
        this.CACHE_GENERAL_STORAGE.copyActualToBuffer();
    }

    public void handleRenderOverlay(RenderGameOverlayEvent.Post event)
    {
       // if (!GameDebuggerData.ConfigDataMonitor.Instance.getDebugMonitorCache())
        //{
        //    return;
        //}

        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT)
        {
            cacheMonitor.renderDebugInfo(event.getResolution());
        }
    }

    public void handleEntityJoinWorld(EntityJoinWorldEvent event)
    {
        World world = event.getWorld();
        Entity entity = event.getEntity();

        if (world.isRemote || !(world instanceof WorldServer))
        {
            return;
        }

        WorldServer worldServer = (WorldServer) world;

        this.CACHE_GENERAL_STORAGE.updateCache(worldServer);

        if (this.CACHE_GENERAL_STORAGE.CACHE_VALID_CHUNKS.contains(new ChunkPos(entity.chunkCoordX, entity.chunkCoordZ)))
        {
            if (entity instanceof IAnimals)
            {
                if (entity instanceof EntityAnimal)
                {
                    this.CACHE_GENERAL_STORAGE.CACHED_ACTUAL_ANIMALS.add((EntityAnimal) entity);
                }
                else if (entity instanceof EntityMob)
                {
                    this.CACHE_GENERAL_STORAGE.CACHED_ACTUAL_HOSTILES.add((IAnimals) entity);
                }
            }

            if (entity instanceof EntityLivingBase)
            {
                String entityName = entity.getName();

                this.CACHE_GENERAL_STORAGE.CACHED_ACTUAL_ALL.add((EntityLivingBase) entity);

                this.CACHE_GENERAL_STORAGE.ENTITIES_ACTUAL_BY_NAME.computeIfAbsent(entityName, k ->
                        new HashSet<>()).add((EntityLivingBase) entity);

                ResourceLocation resourceLocation = EntityList.getKey(entity);

                if (resourceLocation != null)
                {
                    this.CACHE_GENERAL_STORAGE.ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.computeIfAbsent(resourceLocation, k ->
                            new HashSet<>()).add((EntityLivingBase) entity);
                }
            }
        }
    }

    public void handleEntitySpawnEvent(LivingSpawnEvent.CheckSpawn event)
    {
        Entity entity = event.getEntity();
        ResourceLocation entityKey = EntityList.getKey(entity);

        if (entityKey != null)
        {
            this.CACHE_ENTITY_STORAGE.getEntityDataByResourceLocation(entityKey)
                .ifPresent(entityData ->
            {
                int maxCount = entityData.MAX_COUNT;
                int currentCount = CacheGeneralStorage.getInstance().getEntitiesByResourceLocation(entityKey).size();

                if (currentCount > maxCount)
                {
                    event.setResult(Event.Result.DENY);
                }
            });
        }
    }
}
