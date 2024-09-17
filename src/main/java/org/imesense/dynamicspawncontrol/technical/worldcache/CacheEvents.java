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
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.technical.configs.SettingsGameDebugger;

import java.util.HashSet;

/**
 *
 */
public class CacheEvents
{
    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public synchronized void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            Cache.TickCounter++;

            if (Cache.TickCounter >= Cache.UPDATE_INTERVAL)
            {
                Cache.TickCounter = 0;
                Cache.updateCacheAsync(event.world);
            }
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public synchronized void onRenderOverlay(RenderGameOverlayEvent.Post event)
    {
        if (!SettingsGameDebugger.DebugMonitorCache)
        {
            return;
        }

        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT)
        {
            CacheMonitor.renderDebugInfo(event.getResolution());
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public synchronized void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        World world = event.getWorld();
        Entity entity = event.getEntity();

        if (world.isRemote || !(world instanceof WorldServer))
        {
            return;
        }

        WorldServer worldServer = (WorldServer) world;

        Cache.updateCache(worldServer);

        if (Cache.CachedValidChunks.contains(new ChunkPos(entity.chunkCoordX, entity.chunkCoordZ)))
        {
            if (entity instanceof IAnimals)
            {
                if (entity instanceof EntityAnimal)
                {
                    Cache.CachedAnimals.add((EntityAnimal) entity);
                }
                else if (entity instanceof EntityMob)
                {
                    Cache.CachedHostileEntities.add((IAnimals) entity);
                }
            }

            if (entity instanceof EntityLivingBase)
            {
                Cache.CachedAllEntities.add((EntityLivingBase) entity);
                String entityName = entity.getName();
                Cache.EntitiesByName.computeIfAbsent(entityName, k -> new HashSet<>()).add((EntityLivingBase) entity);

                ResourceLocation entityKey = EntityList.getKey(entity);

                if (entityKey != null)
                {
                    Cache.EntitiesByResourceLocation.computeIfAbsent(entityKey, k -> new HashSet<>()).add((EntityLivingBase) entity);
                }
            }
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.HIGH)
    public synchronized void updateEntitySpawnEvent(LivingSpawnEvent.CheckSpawn event)
    {
        Entity entity = event.getEntity();
        ResourceLocation entityKey = EntityList.getKey(entity);

        CacheStorage.EntityData entityData = CacheStorage.getInstance().getEntityDataByResourceLocation(entityKey);

        if (entityData != null)
        {
            assert entityKey != null;

            int currentCount = Cache.getEntitiesByResourceLocation(entityKey).size();
            int maxCount = entityData.getMaxCount();

            if (currentCount > maxCount)
            {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
