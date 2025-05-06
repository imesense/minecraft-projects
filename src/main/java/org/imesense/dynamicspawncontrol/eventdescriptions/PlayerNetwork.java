package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.config.player.PlayerConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.ArrayList;
import java.util.List;

@InitLog
public final class PlayerNetwork
{
    private static volatile PlayerNetwork _INSTANCE;

    private static final ArrayList<String> PLAYER_LIST = new ArrayList<>();

    public static PlayerNetwork getInstance()
    {
        return CodeGeneric.getInstance(PlayerNetwork.class);
    }

    public PlayerNetwork()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handlePlayerJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityPlayerMP && !(event.getEntity() instanceof FakePlayer))
        {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.getEntity();

            if (!PLAYER_LIST.contains(entityPlayerMP.getName()))
            {
                PLAYER_LIST.add(entityPlayerMP.getName());
                Log.write(0, String.format("Player [%s] has been added to the list", entityPlayerMP.getName()));
            }
        }
    }

    public void handlePlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
    {
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.player;
        PLAYER_LIST.remove(entityPlayerMP.getName());
        Log.write(0, String.format("Player [%s] has been removed from the list", entityPlayerMP.getName()));
    }

    public void handlePlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.player;
        World world = entityPlayerMP.world;
        BlockPos playerPos = entityPlayerMP.getPosition();

        int radius =
                PlayerConfig.getInstance(PlayerConfig.class).getProtectRespawnPlayerRadius();

        AxisAlignedBB area = new AxisAlignedBB(
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

                Log.write(0, entityInfo);

                entity.setDead();
            }
        }
    }

    public static boolean isNotSingle()
    {
        return PLAYER_LIST.size() > 1;
    }
}
