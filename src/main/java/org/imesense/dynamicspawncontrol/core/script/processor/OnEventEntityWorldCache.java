package org.imesense.dynamicspawncontrol.core.script.processor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.EntityWorldCache.storage.GeneralEntityWorldCache;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class OnEventEntityWorldCache
{
    private static volatile OnEventEntityWorldCache _INSTANCE;

    public static OnEventEntityWorldCache getInstance()
    {
        return CodeGeneric.getInstance(OnEventEntityWorldCache.class);
    }

    public void handleLivingSpawnEventCheckSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        WorldServer worldServer = (WorldServer) event.getWorld();

        if (worldServer == null)
        {
            Log.writeDataToLogFile(0, "World is null in spawn event. Skipping.");
            return;
        }

        ResourceLocation entityResource = EntityList.getKey(event.getEntity());

        if (entityResource == null)
        {
            Log.writeDataToLogFile(0, "Entity resource location is null. Skipping.");
            return;
        }

        Log.writeDataToLogFile(0, "Processing spawn event for entity: " + entityResource);

        GeneralEntityWorldCache generalEntityWorldCache = GeneralEntityWorldCache.getInstance();
        List<GeneralEntityWorldCache.EntityWorldCacheData> cacheDataList = generalEntityWorldCache.entityWorldCacheDataList;

        boolean found = false;

        for (GeneralEntityWorldCache.EntityWorldCacheData cacheData : cacheDataList)
        {
            if (cacheData.entity.equals(entityResource.toString()))
            {
                Log.writeDataToLogFile(0, "Entity found in cache: " + entityResource);

                applyEntityParameters(event, cacheData, worldServer, entityResource);
                found = true;
                break;
            }
        }

        if (!found)
        {
            Log.writeDataToLogFile(0, "No cache data found for entity: " + entityResource + ". Skipping.");
        }
    }

    private void applyEntityParameters(LivingSpawnEvent.CheckSpawn event, GeneralEntityWorldCache.EntityWorldCacheData cacheData, WorldServer worldServer, ResourceLocation entityResource)
    {
        EntityPlayerMP player = getNearestPlayer(worldServer, event.getX(), event.getY(), event.getZ());

        if (player == null)
        {
            Log.writeDataToLogFile(0, "No player found near spawn event. Skipping.");
            return;
        }

        Log.writeDataToLogFile(0, "Applying parameters for entity: " + entityResource);
        Log.writeDataToLogFile(0, "Per Player: " + cacheData.per_player);
        Log.writeDataToLogFile(0, "Per Chunk: " + cacheData.per_chunk);
        Log.writeDataToLogFile(0, "Max Entity Count: " + cacheData.max_entity_count);
        Log.writeDataToLogFile(0, "Min Entity Count: " + cacheData.min_entity_count);
        Log.writeDataToLogFile(0, "Result: " + cacheData.result);

        int currentEntityCount = getCurrentEntityCount(worldServer, player, entityResource);
        Log.writeDataToLogFile(0, "Current entity count: " + currentEntityCount);

        if (cacheData.per_player != null && cacheData.per_player)
        {
            int playerCount = getPlayerCount(worldServer);
            double multiplier = 0.5 * playerCount;

            if (cacheData.max_entity_count != null)
            {
                cacheData.max_entity_count = (int) (cacheData.max_entity_count * multiplier);
            }

            if (cacheData.min_entity_count != null)
            {
                cacheData.min_entity_count = (int) (cacheData.min_entity_count * multiplier);
            }

            Log.writeDataToLogFile(0, "Adjusted Max Entity Count (per player): " + cacheData.max_entity_count);
            Log.writeDataToLogFile(0, "Adjusted Min Entity Count (per player): " + cacheData.min_entity_count);
        }

        if (cacheData.per_chunk != null && cacheData.per_chunk)
        {
            int loadedChunks = getLoadedChunkCount(worldServer, player);
            double chunkMultiplier = loadedChunks / 289.0;

            if (cacheData.max_entity_count != null)
            {
                cacheData.max_entity_count = (int) (cacheData.max_entity_count * chunkMultiplier);
            }

            if (cacheData.min_entity_count != null)
            {
                cacheData.min_entity_count = (int) (cacheData.min_entity_count * chunkMultiplier);
            }

            Log.writeDataToLogFile(0, "Adjusted Max Entity Count (per chunk): " + cacheData.max_entity_count);
            Log.writeDataToLogFile(0, "Adjusted Min Entity Count (per chunk): " + cacheData.min_entity_count);
        }

        if (cacheData.max_entity_count != null && currentEntityCount >= cacheData.max_entity_count)
        {
            event.setResult(Event.Result.DENY);
            Log.writeDataToLogFile(0, "Denied spawn for " + cacheData.entity + ": max entity count reached (" + cacheData.max_entity_count + ")");
            return;
        }

        if (cacheData.min_entity_count != null && currentEntityCount < cacheData.min_entity_count)
        {
            event.setResult(Event.Result.ALLOW);
            Log.writeDataToLogFile(0, "Allowed spawn for " + cacheData.entity + ": below min entity count (" + cacheData.min_entity_count + ")");
            return;
        }

        if (cacheData.result != null)
        {
            event.setResult(Event.Result.valueOf(cacheData.result.toUpperCase()));
            Log.writeDataToLogFile(0, "Set spawn result for " + cacheData.entity + ": " + cacheData.result);
        }
        else
        {
            Log.writeDataToLogFile(0, "No result specified for entity: " + cacheData.entity + ". Using default.");
            event.setResult(Event.Result.DEFAULT);
        }
    }

    private EntityPlayerMP getNearestPlayer(WorldServer worldServer, double x, double y, double z)
    {
        return (EntityPlayerMP) worldServer.getClosestPlayer(x, y, z, -1, false);
    }

    private int getLoadedChunkCount(WorldServer worldServer, EntityPlayerMP player)
    {
        Set<ChunkPos> validChunks = totalValidChunksSpawnForPlayer(worldServer, player);
        return validChunks.size();
    }

    private int getPlayerCount(WorldServer worldServer)
    {
        return worldServer.getPlayers(EntityPlayerMP.class, player -> true).size();
    }

    private int getCurrentEntityCount(WorldServer worldServer, EntityPlayerMP player, ResourceLocation entityResource)
    {
        int count = 0;
        Set<ChunkPos> validChunks = totalValidChunksSpawnForPlayer(worldServer, player);

        for (ChunkPos chunkPos : validChunks)
        {
            Chunk chunk = worldServer.getChunkFromChunkCoords(chunkPos.x, chunkPos.z);

            for (ClassInheritanceMultiMap<Entity> entityList : chunk.getEntityLists())
            {
                for (Entity entity : entityList)
                {
                    if (entity instanceof EntityLiving)
                    {
                        ResourceLocation entityLoc = EntityList.getKey(entity);

                        if (entityLoc != null && entityLoc.equals(entityResource))
                        {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }

    private Set<ChunkPos> totalValidChunksSpawnForPlayer(WorldServer worldServer, EntityPlayerMP player)
    {
        Set<ChunkPos> validChunks = new HashSet<>();

        int viewDistance = worldServer.getMinecraftServer().getPlayerList().getViewDistance();

        int playerChunkX = MathHelper.floor(player.posX) >> 4;
        int playerChunkZ = MathHelper.floor(player.posZ) >> 4;

        for (int x = playerChunkX - viewDistance; x <= playerChunkX + viewDistance; x++)
        {
            for (int z = playerChunkZ - viewDistance; z <= playerChunkZ + viewDistance; z++)
            {
                ChunkPos chunkPos = new ChunkPos(x, z);

                if (worldServer.getChunkProvider().isChunkGeneratedAt(x, z))
                {
                    validChunks.add(chunkPos);
                }
            }
        }

        return validChunks;
    }
}