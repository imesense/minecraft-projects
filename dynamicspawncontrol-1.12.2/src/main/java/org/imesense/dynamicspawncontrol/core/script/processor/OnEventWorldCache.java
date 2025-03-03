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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheGeneralStorage;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheEntityStorage;
import org.imesense.dynamicspawncontrol.core.worldcache.CacheMonitorDebug;

import java.util.HashSet;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventWorldCache
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    private static CacheMonitorDebug cacheMonitor = null;

    /**
     *
     */
    public OnEventWorldCache()
    {
		CodeGeneric.printInitClassToLog(this.getClass());
		
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        cacheMonitor = new CacheMonitorDebug();
    }

    /**
     *
     * @param worldTickEvent
     */
    @SubscribeEvent
    public void onWorldTick_0(TickEvent.WorldTickEvent worldTickEvent)
    {
        if (worldTickEvent.phase == TickEvent.Phase.END)
        {
            CacheGeneralStorage.Instance.TickCounter++;

            if (CacheGeneralStorage.Instance.TickCounter >= CacheGeneralStorage.Instance._DYNAMIC_UPDATE_INTERVAL)
            {
                CacheGeneralStorage.Instance.TickCounter = 0;

                CacheGeneralStorage.Instance.copyActualToBuffer();
                CacheGeneralStorage.Instance.updateCache(worldTickEvent.world);

                if (CacheGeneralStorage.Instance.IsFirstUpdate)
                {
                    CacheGeneralStorage.Instance._DYNAMIC_UPDATE_INTERVAL =
                            CacheGeneralStorage.Instance.SUBSEQUENT_UPDATE_INTERVAL;

                    CacheGeneralStorage.Instance.IsFirstUpdate = false;
                }
            }
        }
    }

    /**
     *
     * @param playerLoggedInEvent
     */
    @SubscribeEvent
    public void onPlayerLoggedIn_1(PlayerEvent.PlayerLoggedInEvent playerLoggedInEvent)
    {
        if (!CacheGeneralStorage.Instance.IsPrimaryPlayerLogged)
        {
            CacheGeneralStorage.Instance.IsPrimaryPlayerLogged = true;
            CacheGeneralStorage.Instance._DYNAMIC_UPDATE_INTERVAL = CacheGeneralStorage.Instance.FIRST_UPDATE_INTERVAL;
            CacheGeneralStorage.Instance.TickCounter = 0;
            CacheGeneralStorage.Instance.IsFirstUpdate = true;
        }

        CacheGeneralStorage.Instance.copyActualToBuffer();
    }

    /**
     *
     * @param playerLoggedOutEvent
     */
    @SubscribeEvent
    public void onPlayerLoggedOut_2(PlayerEvent.PlayerLoggedOutEvent playerLoggedOutEvent)
    {
        CacheGeneralStorage.Instance.copyActualToBuffer();
    }

    /**
     *
     * @param post
     */
    @SubscribeEvent
    public void onRenderOverlay_3(RenderGameOverlayEvent.Post post)
    {
       // if (!GameDebuggerData.ConfigDataMonitor.Instance.getDebugMonitorCache())
        //{
        //    return;
        //}

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
    public void onEntityJoinWorld_4(EntityJoinWorldEvent entityJoinWorldEvent)
    {
        World world = entityJoinWorldEvent.getWorld();
        Entity entity = entityJoinWorldEvent.getEntity();

        if (world.isRemote || !(world instanceof WorldServer))
        {
            return;
        }

        WorldServer worldServer = (WorldServer) world;

        CacheGeneralStorage.Instance.updateCache(worldServer);

        if (CacheGeneralStorage.Instance.CACHE_VALID_CHUNKS.contains(new ChunkPos(entity.chunkCoordX, entity.chunkCoordZ)))
        {
            if (entity instanceof IAnimals)
            {
                if (entity instanceof EntityAnimal)
                {
                    CacheGeneralStorage.Instance.CACHED_ACTUAL_ANIMALS.add((EntityAnimal) entity);
                }
                else if (entity instanceof EntityMob)
                {
                    CacheGeneralStorage.Instance.CACHED_ACTUAL_HOSTILES.add((IAnimals) entity);
                }
            }

            if (entity instanceof EntityLivingBase)
            {
                String entityName = entity.getName();

                CacheGeneralStorage.Instance.CACHED_ACTUAL_ALL.add((EntityLivingBase) entity);

                CacheGeneralStorage.Instance.ENTITIES_ACTUAL_BY_NAME.computeIfAbsent(entityName, k ->
                        new HashSet<>()).add((EntityLivingBase) entity);

                ResourceLocation resourceLocation = EntityList.getKey(entity);

                if (resourceLocation != null)
                {
                    CacheGeneralStorage.Instance.ENTITIES_ACTUAL_BY_RESOURCE_LOCATION.computeIfAbsent(resourceLocation, k ->
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
    public void updateEntitySpawnEvent_5(LivingSpawnEvent.CheckSpawn event)
    {
        Entity entity = event.getEntity();

        ResourceLocation entityKey = EntityList.getKey(entity);

        CacheEntityStorage.EntityData entityData = CacheEntityStorage.Instance.getEntityDataByResourceLocation(entityKey);

        if (entityData != null)
        {
            assert entityKey != null;

            int maxCount = entityData.getMaxCount();
            int currentCount = CacheGeneralStorage.Instance.getEntitiesByResourceLocation(entityKey).size();

            if (currentCount > maxCount)
            {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
