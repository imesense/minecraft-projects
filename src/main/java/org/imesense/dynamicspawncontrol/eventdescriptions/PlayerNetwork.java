package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.config.player.PlayerConfig;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
            Log.write(0, String.format(
                    "Player [%s] joined the world",
                    player.getName()
            ));
        }
    }

    public void handlePlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
    {
        EntityPlayerMP player = (EntityPlayerMP) event.player;

        if (ONLINE_PLAYERS.remove(player.getUniqueID()))
        {
            Log.write(0, String.format(
                    "Player [%s] left the world",
                    player.getName()
            ));
        }
    }

    public void handlePlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        if (!(event.player instanceof EntityPlayerMP))
            return;

        EntityPlayerMP player = (EntityPlayerMP) event.player;

        if (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
        {
            Log.write(0, String.format(
                    "[RESPAWN] игрок %s начал респавн в измерении %d",
                    player.getName(),
                    player.dimension
            ));
        }

        player.getServerWorld().addScheduledTask(() ->
                player.getServerWorld().addScheduledTask(() -> {
                    if (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
                    {
                        Log.write(0, String.format(
                                "[RESPAWN] Запуск телепортации мобов для игрока %s",
                                player.getName()
                        ));
                    }

                    int configRadius = PlayerConfig.getInstance(PlayerConfig.class)
                            .getProtectRespawnPlayerRadius();

                    if (configRadius <= 0)
                    {
                        if (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
                        {
                            Log.write(0, "[RESPAWN] Радиус защиты отключен (0 или меньше)");
                        }
                        return;
                    }

                    if (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
                    {
                        Log.write(0, String.format(
                                "[RESPAWN] Радиус защиты: %d блоков",
                                configRadius
                        ));
                    }

                    teleportMobsAwayFromPlayer(player, configRadius);
                })
        );
    }

    private void teleportMobsAwayFromPlayer(EntityPlayerMP player, int radius)
    {
        if (!player.isEntityAlive())
        {
            if (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
            {
                Log.write(0, "[TELEPORT] игрок не жив, отмена");
            }
            return;
        }

        if (player.world.isRemote)
        {
            if (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
            {
                Log.write(0, "[TELEPORT] Клиентская сторона, отмена");
            }
            return;
        }

        WorldServer world = player.getServerWorld();
        BlockPos playerPos = player.getPosition();

        if (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
        {
            Log.write(0, String.format(
                    "[TELEPORT] Позиция игрока: X=%d Y=%d Z=%d",
                    playerPos.getX(), playerPos.getY(), playerPos.getZ()
            ));
            Log.write(0, String.format(
                    "[TELEPORT] Дистанция телепортации: %d-%d блоков",
                    MIN_TELEPORT_DISTANCE, MAX_TELEPORT_DISTANCE
            ));
        }

        List<Entity> mobsToTeleport = new ArrayList<>();
        int scannedEntities = 0;

        long scanStart = System.nanoTime();
        for (Entity entity : world.loadedEntityList)
        {
            scannedEntities++;

            if (entity instanceof IMob && !entity.isDead && entity instanceof EntityLiving)
            {
                double distanceSq = entity.getDistanceSq(
                        playerPos.getX(),
                        playerPos.getY(),
                        playerPos.getZ()
                );

                if (distanceSq <= radius * radius)
                {
                    mobsToTeleport.add(entity);

                    if (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG && mobsToTeleport.size() <= 5)
                    {
                        Log.write(0, String.format(
                                "[TELEPORT] Найден моб: %s (расстояние: %.1f)",
                                entity.getClass().getSimpleName(),
                                Math.sqrt(distanceSq)
                        ));
                    }
                }
            }
        }
        long scanTime = System.nanoTime() - scanStart;

        if (mobsToTeleport.isEmpty())
        {
            if (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
            {
                Log.write(0, String.format(
                        "[TELEPORT] Нет мобов для телепортации (проверено: %d сущностей, время: %.2f мс)",
                        scannedEntities,
                        scanTime / 1_000_000.0
                ));
            }
            return;
        }

        if (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
        {
            Log.write(0, String.format(
                    "[TELEPORT] Найдено мобов: %d (проверено: %d, время: %.2f мс)",
                    mobsToTeleport.size(),
                    scannedEntities,
                    scanTime / 1_000_000.0
            ));
        }

        Random rand = world.rand;
        int teleportedCount = 0;
        int failedCount = 0;
        long teleportStart = System.nanoTime();

        for (int i = 0; i < mobsToTeleport.size() && teleportedCount < MAX_MOBS_PER_RESPAWN; i++)
        {
            Entity mob = mobsToTeleport.get(i);

            int distance = MIN_TELEPORT_DISTANCE + rand.nextInt(MAX_TELEPORT_DISTANCE - MIN_TELEPORT_DISTANCE + 1);
            double angle = rand.nextDouble() * 2 * Math.PI;

            int offsetX = (int)(distance * Math.cos(angle));
            int offsetZ = (int)(distance * Math.sin(angle));

            BlockPos targetPos = playerPos.add(offsetX, 0, offsetZ);

            if (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
            {
                Log.write(0, String.format(
                        "[TELEPORT] Моб %d: цель X=%d Z=%d (расстояние: %d, угол: %.0f°)",
                        i + 1,
                        targetPos.getX(),
                        targetPos.getZ(),
                        distance,
                        Math.toDegrees(angle)
                ));
            }

            BlockPos safePos = findSafeTeleportPosition(world, targetPos);

            if (safePos != null)
            {
                double actualDistance = Math.sqrt(safePos.distanceSq(playerPos));
                if (actualDistance > MAX_TELEPORT_DISTANCE * 1.5)
                {
                    if (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
                    {
                        Log.write(0, String.format(
                                "[TELEPORT] Позиция слишком далеко (%.1f блоков), пропускаем",
                                actualDistance
                        ));
                    }
                    failedCount++;
                    continue;
                }

                BlockPos oldPos = mob.getPosition();
                mob.setPositionAndUpdate(
                        safePos.getX() + 0.5,
                        safePos.getY(),
                        safePos.getZ() + 0.5
                );
                teleportedCount++;

                if (mob instanceof EntityLiving)
                {
                    ((EntityLiving) mob).addPotionEffect(new PotionEffect(
                            MobEffects.SLOWNESS,
                            SLOWNESS_DURATION,
                            SLOWNESS_AMPLIFIER,
                            false, false
                    ));
                }

                if (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
                {
                    double newDistance = Math.sqrt(mob.getDistanceSq(playerPos));
                    Log.write(0, String.format(
                            "[TELEPORT] Телепортирован: %s с [%d,%d,%d] на [%d,%d,%d] (новое расстояние: %.1f)",
                            mob.getClass().getSimpleName(),
                            oldPos.getX(), oldPos.getY(), oldPos.getZ(),
                            safePos.getX(), safePos.getY(), safePos.getZ(),
                            newDistance
                    ));
                }
            }
            else
            {
                failedCount++;
                if (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
                {
                    Log.write(0, String.format(
                            "[TELEPORT] Не удалось найти безопасную позицию для моба %s",
                            mob.getClass().getSimpleName()
                    ));
                }
            }
        }

        long totalTime = System.nanoTime() - teleportStart + scanTime;

        String result = String.format(
                "Защита при респавне: телепортировано %d из %d мобов от игрока %s (радиус: %d, время: %.2f мс)",
                teleportedCount,
                mobsToTeleport.size(),
                player.getName(),
                radius,
                totalTime / 1_000_000.0
        );

        Log.write(1, result);

        if (UniqueField.LOGGING_CONSOLE_LEVEL_DEBUG)
        {
            Log.write(0, "[Result] " + result);
            if (failedCount > 0)
            {
                Log.write(0, String.format(
                        "[Result] Не удалось телепортировать %d мобов",
                        failedCount
                ));
            }
        }
    }

    private BlockPos findSafeTeleportPosition(WorldServer world, BlockPos center)
    {
        Random rand = world.rand;

        for (int attempt = 0; attempt < SAFE_POSITION_ATTEMPTS; attempt++)
        {
            int offsetX = rand.nextInt(SEARCH_AREA_RADIUS * 2 + 1) - SEARCH_AREA_RADIUS;
            int offsetZ = rand.nextInt(SEARCH_AREA_RADIUS * 2 + 1) - SEARCH_AREA_RADIUS;

            BlockPos testPos = center.add(offsetX, 0, offsetZ);
            BlockPos surfacePos = world.getTopSolidOrLiquidBlock(testPos);

            if (isPositionSafeForTeleport(world, surfacePos))
            {
                return surfacePos;
            }
        }

        return world.getTopSolidOrLiquidBlock(center);
    }

    private boolean isPositionSafeForTeleport(WorldServer world, BlockPos pos)
    {
        if (!world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP))
            return false;

        if (!world.isAirBlock(pos) || !world.isAirBlock(pos.up()))
            return false;

        net.minecraft.block.Block blockBelow = world.getBlockState(pos.down()).getBlock();
        return blockBelow != Blocks.LAVA &&
                blockBelow != Blocks.FLOWING_LAVA &&
                blockBelow != Blocks.FIRE &&
                blockBelow != Blocks.MAGMA;
    }

    public static boolean isNotSingle()
    {
        return ONLINE_PLAYERS.size() > 1;
    }
}
