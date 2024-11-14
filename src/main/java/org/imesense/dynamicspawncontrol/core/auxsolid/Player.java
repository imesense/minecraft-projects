package org.imesense.dynamicspawncontrol.core.auxsolid;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Objects;

/**
 *
 */
public final class Player
{
    /**
     *
     * @param entity
     * @return
     */
    public static boolean isFakePlayer(Entity entity)
    {
        if (!(entity instanceof EntityPlayerMP))
        {
            return false;
        }

        if (entity instanceof FakePlayer)
        {
            return true;
        }

        // If this method returns false, it is still possible that it is a fake player. Let's try to find a player in the list of online players
        PlayerList playerList = Objects.requireNonNull(DimensionManager.getWorld(0).getMinecraftServer()).getPlayerList();
        EntityPlayerMP playerByUUID = playerList.getPlayerByUUID(((EntityPlayerMP) entity).getGameProfile().getId());

        if (playerByUUID == null)
        {
            // The player is offline. So it can't be a real player
            return true;
        }

        // The player in the list. But is this the right player?
        return entity != playerByUUID;
    }

    /**
     *
     * @param entity
     * @return
     */
    public static boolean isRealPlayer(Entity entity)
    {
        if (!(entity instanceof EntityPlayerMP))
        {
            return false;
        }

        return !isFakePlayer(entity);
    }
}
