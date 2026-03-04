package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@InitLog
@TODO(value = "Rework for 0.2 ver. Separate the methods that are related to 'handlePlayerRespawn'. Add const's in config", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class PlayerNetwork
{
    private static volatile PlayerNetwork _INSTANCE;

    private static final Set<UUID> ONLINE_PLAYERS =
            ConcurrentHashMap.newKeySet();

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
        if (!(event.getEntity() instanceof EntityPlayerMP))
            return;

        if (event.getEntity() instanceof FakePlayer)
            return;

        EntityPlayerMP player = (EntityPlayerMP) event.getEntity();

        if (ONLINE_PLAYERS.add(player.getUniqueID()))
        {
            LogManager.info(String.format("Player [%s] joined the world", player.getName()));
        }
    }

    public void handlePlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
    {
        EntityPlayerMP player = (EntityPlayerMP) event.player;

        if (ONLINE_PLAYERS.remove(player.getUniqueID()))
        {
            LogManager.info(String.format("Player [%s] left the world", player.getName()));
        }
    }

    public void handlePlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {

    }
}
