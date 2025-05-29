package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
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
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheFunctional;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheGeneralStorage;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheEntityStorage;

import java.util.HashSet;
import java.util.Optional;

@InitLog
@TODO(
        value = "Пофиксить учет сущностей, у которых используется ключевое слово в парсере 'instanceof'. И добавить логгирование",
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
            .filter(data ->
            {
                if (data.check_instanceof != null)
                {
                    return data.check_instanceof.isInstance(entity);
                }
                else if (data.entity != null && entityKey != null)
                {
                    return data.entity.equals(entityKey);
                }
                return false;
            })
        .findFirst();

        if (!optionalEntityData.isPresent())
        {
            return;
        }

        CacheEntityStorage.EntityData entityData = optionalEntityData.get();

        if (entityData.check_instanceof != null)
        {
            boolean isZombie = EntityZombie.class.equals(entityData.check_instanceof);
            boolean isPigZombie = EntityPigZombie.class.equals(entityData.check_instanceof);

            if ((isZombie || isPigZombie) && !event.getWorld().isRemote)
            {
                int zombieCount = event.getWorld().getEntities(
                        isZombie ? EntityZombie.class : EntityPigZombie.class,
                        e -> true
                ).size();

                //Log.write(0,
                //        (isZombie ? "Zombie" : "Pig Zombie") +
                //                " spawn attempt. Current count: " + zombieCount);

                if (entityData.max_entity_count != null && zombieCount >= entityData.max_entity_count)
                {
                    event.setResult(entityData.result);

                    //Log.write(0,
                    //        (isZombie ? "Zombie" : "Pig Zombie") +
                    //                " spawn blocked! Limit reached (" +
                    //                zombieCount + "/" + entityData.max_entity_count + ")");

                    return;
                }
            }
        }

        WorldServer worldServer = (WorldServer) event.getWorld();
        EntityPlayerMP nearestPlayer = CacheFunctional.getInstance()
                .getNearestPlayer(worldServer, event.getX(), event.getY(), event.getZ());

        if (nearestPlayer == null)
        {
            return;
        }

        ResourceLocation countKey = entityData.entity != null ? entityData.entity : entityKey;

        int currentEntityCount = CacheFunctional.getInstance()
                .getCurrentEntityCount(worldServer, nearestPlayer, countKey);

        int maxEntityCount = CacheFunctional.getInstance()
                .calculateMaxEntityCount(entityData, worldServer, nearestPlayer);

        //Log.write(0, "Entity: " + countKey +
        //        ", Current Count: " + currentEntityCount +
        //        ", Max Count: " + maxEntityCount);

        if (currentEntityCount >= maxEntityCount)
        {
            event.setResult(entityData.result);
        }
    }
}
