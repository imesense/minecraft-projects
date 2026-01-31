package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
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
import org.imesense.dynamicspawncontrol.core.worldcache.*;

import java.util.HashSet;
import java.util.Optional;

@InitLog
@TODO(
        value = "Fix the accounting of entities, which use the keyword 'instanceof' in the parser. And add logging + fix diagram and break optimization",
        priority = TODO.TodoPriority.HIGH,
        showOnce = false
)
public final class OnEventWorldCacheOld
{
    private static volatile OnEventWorldCacheOld _INSTANCE;

    public static OnEventWorldCacheOld getInstance()
    {
        return CodeGeneric.getInstance(OnEventWorldCacheOld.class);
    }

    private final CacheGeneralStorage CACHE_GENERAL_STORAGE = CacheGeneralStorage.getInstance();
    private final CacheGameEventStorage GAME_EVENT_STORAGE = CacheGameEventStorage.getInstance();

    //-' Счетчик дней для отслеживания игрового времени
    private long lastWorldTime = 0;
    private int currentDay = 0;

    //-' Логирование состояния событий
    private boolean debugGameEvents = true;

    public OnEventWorldCacheOld()
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
            //-' Обновление счетчика тиков
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

            //-' Проверка смены игрового дня
            checkDayChange(event.world);
        }
    }

    /**
     * -' Проверяет смену игрового дня и активирует события
     */
    private void checkDayChange(World world)
    {
        if (world.isRemote || !(world instanceof WorldServer))
        {
            return;
        }

        long currentWorldTime = world.getWorldTime();

        //-' Проверяем, сменился ли день (24,000 тиков = 1 игровой день)
        int newDay = (int)(currentWorldTime / 24000L);

        if (newDay != currentDay)
        {
            int oldDay = currentDay;
            currentDay = newDay;
            lastWorldTime = currentWorldTime;

            if (debugGameEvents)
            {
                Log.write(0, String.format("Day changed: %d -> %d (WorldTime: %d)",
                        oldDay, currentDay, currentWorldTime));
            }

            //-' Активируем события для нового дня
            activateGameEventsForDay(currentDay, (WorldServer) world);
        }
    }

    /**
     * -' Активирует все события для указанного дня
     */
    private void activateGameEventsForDay(int day, WorldServer world)
    {
        if (GAME_EVENT_STORAGE.eventData.isEmpty())
        {
            return;
        }

        int dimension = world.provider.getDimension();

        for (CacheGameEventStorage.GameEventData eventData : GAME_EVENT_STORAGE.eventData)
        {
            //-' Проверяем измерение
            if (eventData.idDimension != null && eventData.idDimension != dimension)
            {
                continue;
            }

            //-' Проверяем условие дня
            boolean dayCondition = false;
            if (eventData.repeat)
            {
                //-' Для повторяющихся событий: день должен быть кратен указанному, но не 0
                if (eventData.day > 0 && day > 0 && day % eventData.day == 0)
                {
                    dayCondition = true;
                }
            }
            else
            {
                //-' Для неповторяющихся: точное совпадение дня
                dayCondition = (day == eventData.day);
            }

            if (dayCondition)
            {
                //-' Активируем событие
                activateGameEvent(eventData, day, world);
            }
        }
    }

    /**
     * -' Активирует конкретное игровое событие
     */
    private void activateGameEvent(CacheGameEventStorage.GameEventData eventData, int day, WorldServer world)
    {
        if (debugGameEvents)
        {
            String eventType = eventData.repeat ? "Repeating" : "One-time";
            Log.write(0, String.format("Game Event ACTIVATED - Type: %s, Day: %d, Entity: %s, " +
                            "Max Count: %d, Dimension: %d, Result: %s",
                    eventType, day, eventData.entity,
                    eventData.max_entity_count,
                    world.provider.getDimension(),
                    eventData.result));
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
        World world = event.getWorld();
        int currentDimension = world.provider.getDimension();
        ResourceLocation entityKey = EntityList.getKey(entity);

        //-' Проверка игровых событий в первую очередь
        if (checkGameEvents(event, entity, world, currentDimension, entityKey))
        {
            return;
        }

        //-' Оригинальная проверка (остается как резерв)
        if (currentDimension == 0 && (entity instanceof IAnimals && !(entity instanceof EntityMob)
                && !WorldCacheConfig.getInstance(WorldCacheConfig.class).isSpawnPeacefulCreaturesAtNight()))
        {
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
            if (data.idDimension != null && data.idDimension != currentDimension)
            {
                return false;
            }

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

        if (entityData.isContinue != null && entityData.isContinue)
        {
            if (entityData.result != null)
            {
                event.setResult(entityData.result);
            }

            Log.write(0, String.format("Entity %s in dimension %d processed with continue: true, result: %s",
                    entityKey, currentDimension, entityData.result != null ? entityData.result : "DEFAULT"));
            return;
        }

        if (entityData.check_instanceof != null)
        {
            boolean isZombie = EntityZombie.class.equals(entityData.check_instanceof);
            boolean isPigZombie = EntityPigZombie.class.equals(entityData.check_instanceof);

            if ((isZombie || isPigZombie) && !event.getWorld().isRemote)
            {
                int zombieCount = (int) event.getWorld().loadedEntityList.stream()
                .filter(e ->
                {
                    if (isZombie && e instanceof EntityZombie) return true;
                    if (isPigZombie && e instanceof EntityPigZombie) return true;
                    return false;
                })
                .count();

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

    /**
     * -' Проверяет игровые события для текущего спавна
     * @return true если событие обработано
     */
    private boolean checkGameEvents(LivingSpawnEvent.CheckSpawn event, Entity entity, World world,
                                    int dimension, ResourceLocation entityKey)
    {
        if (GAME_EVENT_STORAGE.eventData.isEmpty() || entityKey == null)
        {
            return false;
        }

        int currentDay = (int)(world.getWorldTime() / 24000L);

        for (CacheGameEventStorage.GameEventData eventData : GAME_EVENT_STORAGE.eventData)
        {
            //-' Проверяем измерение
            if (eventData.idDimension != null && eventData.idDimension != dimension)
            {
                continue;
            }

            //-' Проверяем сущность
            if (!eventData.entity.equals(entityKey))
            {
                continue;
            }

            //-' Проверяем условие дня
            boolean dayCondition = false;
            if (eventData.repeat)
            {
                //-' Для повторяющихся событий: день должен быть кратен указанному, но не 0
                if (eventData.day > 0 && currentDay > 0 && currentDay % eventData.day == 0)
                {
                    dayCondition = true;
                }
            }
            else
            {
                //-' Для неповторяющихся: точное совпадение дня
                dayCondition = (currentDay == eventData.day);
            }

            if (dayCondition)
            {
                //-' Событие активно! Применяем ограничения
                int currentCount = getCurrentEntityCount(world, entityKey);

                if (debugGameEvents)
                {
                    String eventType = eventData.repeat ? "Repeating" : "One-time";
                    Log.write(0, String.format("Game Event ACTIVE for spawn - Type: %s, Day: %d, Entity: %s, " +
                                    "Checking limit: %d/%d",
                            eventType, currentDay, entityKey,
                            currentCount,
                            eventData.max_entity_count));

                    //-' Дополнительная отладка
                    if (currentCount == 0)
                    {
                        Log.write(0, String.format("DEBUG: No %s entities found in world. Total entities: %d",
                                entityKey, world.loadedEntityList.size()));
                        //-' Выведем типы всех сущностей для отладки
                        for (Object obj : world.loadedEntityList)
                        {
                            if (obj instanceof Entity)
                            {
                                Entity e = (Entity) obj;
                                ResourceLocation key = EntityList.getKey(e);
                                Log.write(0, String.format("DEBUG: Entity: %s, Key: %s",
                                        e.getClass().getSimpleName(), key));
                            }
                        }
                    }
                }

                //-' Проверяем лимит сущностей
                if (currentCount >= eventData.max_entity_count)
                {
                    event.setResult(eventData.result);

                    if (debugGameEvents)
                    {
                        Log.write(0, String.format("Game Event BLOCKED spawn - Entity: %s, Count: %d/%d, Result: %s",
                                entityKey, currentCount, eventData.max_entity_count, eventData.result));
                    }

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * -' Получает текущее количество сущностей определенного типа в мире
     */
    private int getCurrentEntityCount(World world, ResourceLocation entityType)
    {
        if (world.loadedEntityList == null || entityType == null)
        {
            return 0;
        }

        int count = 0;
        for (Object obj : world.loadedEntityList)
        {
            if (obj instanceof Entity)
            {
                Entity entity = (Entity) obj;
                ResourceLocation entityKey = EntityList.getKey(entity);
                if (entityKey != null && entityKey.equals(entityType))
                {
                    count++;
                }
                else
                {
                    //-' Также проверяем по имени класса для EntityZombie
                    if (entityType.toString().equals("minecraft:zombie") && entity instanceof EntityZombie)
                    {
                        count++;
                    }
                }
            }
        }

        return count;
    }
}