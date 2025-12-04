package org.imesense.dynamicspawncontrol.core.worldcache;

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
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.HashSet;
import java.util.Set;

@InitLog
public final class CacheFunctional
{
    private static volatile CacheFunctional _INSTANCE;

    public static CacheFunctional getInstance()
    {
        return CodeGeneric.getInstance(CacheFunctional.class);
    }

    public CacheFunctional()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public int calculateMaxEntityCount(CacheEntityStorage.EntityData entityData,
                                       WorldServer worldServer, EntityPlayerMP player)
    {
        int maxEntityCount = entityData.max_entity_count;

        if (entityData.per_player)
        {
            int playerCount = getPlayerCount(worldServer);
            maxEntityCount = maxEntityCount * playerCount;
        }

        if (entityData.per_chunk)
        {
            int loadedChunkCount = getLoadedChunkCount(worldServer, player);
            maxEntityCount = (int) (maxEntityCount * ((double) loadedChunkCount / 289));
        }

        return maxEntityCount;
    }

    public EntityPlayerMP getNearestPlayer(WorldServer worldServer, double x, double y, double z)
    {
        return (EntityPlayerMP) worldServer.getClosestPlayer(x, y, z, -1, false);
    }

    public int getLoadedChunkCount(WorldServer worldServer, EntityPlayerMP player)
    {
        Set<ChunkPos> validChunks = totalValidChunksSpawnForPlayer(worldServer, player);
        return validChunks.size();
    }

    public int getPlayerCount(WorldServer worldServer)
    {
        return worldServer.getPlayers(EntityPlayerMP.class, player -> true).size();
    }

    public int getCurrentEntityCount(WorldServer worldServer, EntityPlayerMP player, ResourceLocation entityResource)
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

    public Set<ChunkPos> totalValidChunksSpawnForPlayer(WorldServer worldServer, EntityPlayerMP player)
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
