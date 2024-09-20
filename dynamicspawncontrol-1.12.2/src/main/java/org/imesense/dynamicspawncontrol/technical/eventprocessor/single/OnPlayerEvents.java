package org.imesense.dynamicspawncontrol.technical.eventprocessor.single;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.imesense.dynamicspawncontrol.technical.configs.ConfigPlayer;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class OnPlayerEvents
{
    /**
     *
     */
    private static final ArrayList<String> PLAYER_LIST = new ArrayList<>();

    /**
     *
     * @param nameClass
     */
    public OnPlayerEvents(final String nameClass)
    {
        Log.writeDataToLogFile(0, nameClass);
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdateEntityJoinWorld_0(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer && !(event.getEntity() instanceof FakePlayer))
        {
            EntityPlayer player = (EntityPlayer) event.getEntity();

            if (!PLAYER_LIST.contains(player.getName()))
            {
                PLAYER_LIST.add(player.getName());
                Log.writeDataToLogFile(0, String.format("Player [%s] has been added to the list", player.getName()));
            }
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdatePlayerLoggedOut_1(PlayerEvent.PlayerLoggedOutEvent event)
    {
        EntityPlayer player = event.player;
        PLAYER_LIST.remove(player.getName());
        Log.writeDataToLogFile(0, String.format("Player [%s] has been removed from the list", player.getName()));
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdatePlayerLogin_0(FMLNetworkEvent.ClientConnectedToServerEvent event)
    {
        Log.writeDataToLogFile(0, "ClientConnectedToServerEvent " + event);
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdatePlayerLoginServer_0(PlayerEvent.PlayerLoggedInEvent event)
    {
        Log.writeDataToLogFile(0, "PlayerLoggedInEvent " + event.player.getName() + " logged in.");
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdatePlayerLogout_0(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        Log.writeDataToLogFile(0, "ClientDisconnectionFromServerEvent " + event);
    }

    /**
     *
     * @return
     */
    public static boolean isNotSingle()
    {
        return PLAYER_LIST.size() > 1;
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerRespawn_0(PlayerEvent.PlayerRespawnEvent event)
    {
        EntityPlayer player = event.player;
        World world = player.world;
        BlockPos playerPos = player.getPosition();

        int radius = ConfigPlayer.ProtectRespawnPlayerRadius;

        AxisAlignedBB area = new AxisAlignedBB
                (
                        playerPos.add(-radius, -radius, -radius),
                        playerPos.add(radius, radius, radius)
                );

        List<Entity> entitiesInArea = world.getEntitiesWithinAABB(Entity.class, area);

        for (Entity entity : entitiesInArea)
        {
            if (entity instanceof IMob)
            {
                String entityInfo = String.format("Deleted entities: %s on the coordinates: X=%.2f, Y=%.2f, Z=%.2f",
                        entity.getName(),
                        entity.posX,
                        entity.posY,
                        entity.posZ
                );

                Log.writeDataToLogFile(0, entityInfo);

                entity.setDead();
            }
        }
    }
}
