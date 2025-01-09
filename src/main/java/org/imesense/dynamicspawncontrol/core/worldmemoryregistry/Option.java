package org.imesense.dynamicspawncontrol.core.worldmemoryregistry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;

public final class Option
{
    public static boolean isOutsideRenderDistance(Entity entity)
    {
        MinecraftServer server = entity.getServer();
        
        if (server == null)
            return false;

        PlayerList playerList = server.getPlayerList();
        int renderDistance = playerList.getViewDistance();

        BlockPos entityPos = entity.getPosition();
        int entityChunkX = entityPos.getX() >> 4;
        int entityChunkZ = entityPos.getZ() >> 4;

        for (EntityPlayer player : entity.world.playerEntities)
        {
            BlockPos playerPos = player.getPosition();
            int playerChunkX = playerPos.getX() >> 4;
            int playerChunkZ = playerPos.getZ() >> 4;

            if (Math.abs(playerChunkX - entityChunkX) <= renderDistance &&
                    Math.abs(playerChunkZ - entityChunkZ) <= renderDistance)
            {
                return false;
            }
        }
        return true;
    }
}
