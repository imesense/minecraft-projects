package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@InitLog
@TODO(value = "Rework for 0.2 ver. Separate the methods that are related to 'handlePlayerRespawn'. Add const's in config", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class PlayerNetwork
{
    private static volatile PlayerNetwork _INSTANCE;

    private static final int MIN_TELEPORT_DISTANCE = 32;   // Минимум 32 блока от игрока
    private static final int MAX_TELEPORT_DISTANCE = 64;   // Максимум 64 блока от игрока
    private static final int MAX_MOBS_PER_RESPAWN = 10;    // Максимум 10 мобов за респавн
    private static final int SAFE_POSITION_ATTEMPTS = 10;  // Попыток найти безопасную позицию
    private static final int SEARCH_AREA_RADIUS = 16;      // Радиус поиска безопасной позиции
    private static final int SLOWNESS_DURATION = 100;      // Длительность замедления (5 сек)
    private static final int SLOWNESS_AMPLIFIER = 2;       // Уровень замедления (3)

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
            Logger.info(String.format("Player [%s] joined the world", player.getName()));
        }
    }

    public void handlePlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
    {
        EntityPlayerMP player = (EntityPlayerMP) event.player;

        if (ONLINE_PLAYERS.remove(player.getUniqueID()))
        {
            Logger.info(String.format("Player [%s] left the world", player.getName()));
        }
    }

    public void handlePlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {

    }
}
