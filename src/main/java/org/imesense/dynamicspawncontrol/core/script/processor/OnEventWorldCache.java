package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.config.worldcache.WorldCacheConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheFunctional;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheGeneralStorage;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheEntityStorage;

import java.util.HashSet;
import java.util.Optional;

/**
 * По факту проблема в том что сущность заменяется на этапе EntityJoinWorldEvent event,
 * а кеш проверяет событие LivingSpawnEvent.CheckSpawn event, т.е проверка кеша стопорится тем,
 * что сущность прошла проверку на спавн, спавнится и она сразу заменяется, и так получается что ограничение по сущности никогда не будет истинным.
 */
@InitLog
@TODO(
        value = "Критическая проблема, решить при первой возможности",
        priority = TODO.TodoPriority.HIGH,
        showOnce = false
)
public final class OnEventWorldCache
{
    private static volatile OnEventWorldCache _INSTANCE;

    public static OnEventWorldCache getInstance()
    {
        return CodeGeneric.getInstance(OnEventWorldCache.class);
    }

    private final CacheGeneralStorage CACHE_GENERAL_STORAGE = CacheGeneralStorage.getInstance();

    public OnEventWorldCache()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
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
                else if (entity instanceof EntityWaterMob)
                {
                    this.CACHE_GENERAL_STORAGE.CACHED_ACTUAL_WATER_MOBS.add((EntityWaterMob) entity);
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

        if (entityKey == null)
        {
            return;
        }

        if ((entity instanceof IAnimals && !(entity instanceof EntityMob)
                && !WorldCacheConfig.getInstance(WorldCacheConfig.class).isSpawnPeacefulCreaturesAtNight()))
        {
            World world = event.getWorld();
            if (!world.isDaytime())
            {
                event.setResult(Event.Result.DENY);
                return;
            }
        }

        Optional<CacheEntityStorage.EntityData> optionalEntityData = CacheEntityStorage.getInstance()
                .entityData.stream()
                .filter(data -> data.entity.equals(entityKey))
                .findFirst();

        if (!optionalEntityData.isPresent())
        {
            return;
        }

        CacheEntityStorage.EntityData entityData = optionalEntityData.get();
        WorldServer worldServer = (WorldServer) event.getWorld();

        EntityPlayerMP nearestPlayer =
                CacheFunctional.getInstance().getNearestPlayer(worldServer, event.getX(), event.getY(), event.getZ());

        if (nearestPlayer == null)
        {
            return;
        }

        int currentEntityCount =
                CacheFunctional.getInstance().getCurrentEntityCount(worldServer, nearestPlayer, entityKey);

        int maxEntityCount =
                CacheFunctional.getInstance().calculateMaxEntityCount(entityData, worldServer, nearestPlayer);

        //Log.writeDataToLogFile(0, "Entity: " + entityKey + ", " +
        //        "Current Count: " + currentEntityCount + ", Max Count: " + maxEntityCount);

        if (currentEntityCount >= maxEntityCount)
        {
            event.setResult(entityData.result);
        }
    }
}
