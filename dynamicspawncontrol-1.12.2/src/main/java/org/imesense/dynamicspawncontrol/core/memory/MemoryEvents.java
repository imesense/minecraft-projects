package org.imesense.dynamicspawncontrol.core.memory;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public final class MemoryEvents
{
    @Getter
    private static long lastCleanTime = 0L;
    private static final Set<UUID> recognizedPlayers = new LinkedHashSet<>();
    private static int idleTime = 0;

    public static void setLastCleanTime(long lastCleanTime)
    {
        MemoryEvents.lastCleanTime = lastCleanTime;
    }

    @SideOnly(Side.CLIENT)
    public static void handleOnClientTick(TickEvent.ClientTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.isGamePaused() || event.phase != TickEvent.ClientTickEvent.Phase.END)
        {
            return;
        }

        EntityPlayerSP player = mc.player;

        if (player == null || !player.world.isRemote)
        {
            return;
        }

        boolean shouldClean = false;

        long currentTime = System.currentTimeMillis();
        long timeSinceLastClean = currentTime - lastCleanTime;

        if (timeSinceLastClean > Configuration.getAutomaticCleanup().getMinInterval() * 1000L)
        {
            Runtime runtime = Runtime.getRuntime();
            double memoryUsage = (double)(runtime.totalMemory() - runtime.freeMemory()) / runtime.maxMemory();

            if (memoryUsage > Configuration.getForceCleanPercentage() / 100.0D)
            {
                shouldClean = true;
            }
            else if (Configuration.getAutomaticCleanup().isAutoCleanup())
            {
                if (idleTime > Configuration.getAutomaticCleanup().getMinIdleTime() * 20)
                {
                    shouldClean = true;
                }
                else if (timeSinceLastClean > Configuration.getAutomaticCleanup().getMaxInterval() * 1000L)
                {
                    shouldClean = true;
                }
            }

            if (shouldClean)
            {
                MemoryManager.cleanMemory(player);
                lastCleanTime = currentTime;
                idleTime = 0;
            }

            if (Configuration.getAutomaticCleanup().isAutoCleanup())
            {
                if (Math.abs(player.motionX) < 0.001D &&
                        Math.abs(player.motionY) < 0.001D &&
                        Math.abs(player.motionZ) < 0.001D)
                {
                    idleTime++;
                }
                else
                {
                    idleTime = 0;
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void handleOnPlayerLogin(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof EntityPlayer) || !event.getWorld().isRemote)
        {
            return;
        }

        EntityPlayer player = (EntityPlayer)event.getEntity();

        if (player.getUniqueID().equals(Minecraft.getMinecraft().player.getUniqueID()))
        {
            if (!recognizedPlayers.contains(player.getUniqueID()))
            {
                if (Configuration.isCleanOnJoin())
                {
                    MemoryManager.cleanMemory(player);
                }

                lastCleanTime = System.currentTimeMillis();

                idleTime = 0;

                recognizedPlayers.add(player.getUniqueID());
            }
        }
    }
}
