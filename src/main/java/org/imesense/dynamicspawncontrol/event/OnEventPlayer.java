package org.imesense.dynamicspawncontrol.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.config.data.PlayerData;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventPlayer
{
    private static boolean instanceExists = false;

    private static final ArrayList<String> PLAYER_LIST = new ArrayList<>();

    public OnEventPlayer()
    {
		CodeGeneric.printInitClassToLog(this.getClass());
		
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    @SubscribeEvent
    public void onUpdateEntityJoinWorld_0(EntityJoinWorldEvent entityJoinWorldEvent)
    {
        if (entityJoinWorldEvent.getEntity() instanceof EntityPlayerMP &&
                !(entityJoinWorldEvent.getEntity() instanceof FakePlayer))
        {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) entityJoinWorldEvent.getEntity();

            if (!PLAYER_LIST.contains(entityPlayerMP.getName()))
            {
                PLAYER_LIST.add(entityPlayerMP.getName());
                Log.writeDataToLogFile(0, String.format("Player [%s] has been added to the list", entityPlayerMP.getName()));
            }
        }
    }

    @SubscribeEvent
    public void onUpdatePlayerLoggedOut_1(PlayerEvent.PlayerLoggedOutEvent playerLoggedOutEvent)
    {
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerLoggedOutEvent.player;
        PLAYER_LIST.remove(entityPlayerMP.getName());
        Log.writeDataToLogFile(0, String.format("Player [%s] has been removed from the list", entityPlayerMP.getName()));
    }

    @SubscribeEvent
    public void onUpdatePlayerLogin_2(FMLNetworkEvent.ClientConnectedToServerEvent clientConnectedToServerEvent)
    {
        Log.writeDataToLogFile(0, "ClientConnectedToServerEvent " + clientConnectedToServerEvent);
    }

    @SubscribeEvent
    public void onUpdatePlayerLoginServer_3(PlayerEvent.PlayerLoggedInEvent playerLoggedInEvent)
    {
        Log.writeDataToLogFile(0, "PlayerLoggedInEvent " + playerLoggedInEvent.player.getName() + " logged in.");
    }

    @SubscribeEvent
    public void onUpdatePlayerLogout_4(FMLNetworkEvent.ClientDisconnectionFromServerEvent clientDisconnectionFromServerEvent)
    {
        Log.writeDataToLogFile(0, "ClientDisconnectionFromServerEvent " + clientDisconnectionFromServerEvent);
    }

    @SubscribeEvent
    public void onPlayerRespawn_5(PlayerEvent.PlayerRespawnEvent playerRespawnEvent)
    {
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerRespawnEvent.player;
        World world = entityPlayerMP.world;
        BlockPos blockPos = entityPlayerMP.getPosition();

        int radius = PlayerData.ConfigDataPlayer.Instance.getProtectRespawnPlayerRadius();

        AxisAlignedBB area = new AxisAlignedBB
        (
            blockPos.add(-radius, -radius, -radius),
            blockPos.add(radius, radius, radius)
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

    public static boolean isNotSingle()
    {
        return PLAYER_LIST.size() > 1;
    }
}
