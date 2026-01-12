package org.imesense.dynamicspawncontrol.core.memory;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public final class MemoryEvents
{
    @Getter
    private static long lastCleanTime = 0L;

    private static long lastWarningTime = 0L;

    private static final Set<UUID> recognizedPlayers = new LinkedHashSet<>();

    public static void setLastCleanTime(long lastCleanTime)
    {
        MemoryEvents.lastCleanTime = lastCleanTime;
    }

    public MemoryEvents()
    {

    }

    @SideOnly(Side.CLIENT)
    public static void handleOnClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.ClientTickEvent.Phase.END)
        {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();

        if (minecraft.isGamePaused())
        {
            return;
        }

        EntityPlayerSP player = minecraft.player;

        if (player == null || !player.world.isRemote)
        {
            return;
        }

        Runtime runtime = Runtime.getRuntime();
        double memoryUsage =
                (double)(runtime.totalMemory() - runtime.freeMemory()) / runtime.maxMemory();

        if (memoryUsage >= Configuration.getForceCleanPercentage() / 100.0D)
        {
            warnHighMemory(player, memoryUsage);
        }
    }

    @SideOnly(Side.CLIENT)
    private static void warnHighMemory(EntityPlayer player, double memoryUsage)
    {
        long now = System.currentTimeMillis();

        if (now - lastWarningTime < 30_000L)
        {
            return;
        }

        lastWarningTime = now;

        int percent = (int)(memoryUsage * 100);

        TextComponentString prefix =
                new TextComponentString("⚠ High memory usage (");
        prefix.getStyle().setColor(TextFormatting.RED);

        TextComponentString value =
                new TextComponentString(percent + "%");
        value.getStyle().setColor(TextFormatting.YELLOW);

        TextComponentString suffix =
                new TextComponentString("). Type ");
        suffix.getStyle().setColor(TextFormatting.RED);

        TextComponentString command =
                new TextComponentString("/dsc_clean_up_memory");
        command.getStyle().setColor(TextFormatting.GREEN);

        TextComponentString end =
                new TextComponentString(" when safe.");
        end.getStyle().setColor(TextFormatting.RED);

        prefix.appendSibling(value);
        prefix.appendSibling(suffix);
        prefix.appendSibling(command);
        prefix.appendSibling(end);

        player.sendMessage(prefix);
    }

    @SideOnly(Side.CLIENT)
    public static void handleOnPlayerLogin(EntityJoinWorldEvent event)
    {
        if (!(event.getEntity() instanceof EntityPlayer) || !event.getWorld().isRemote)
        {
            return;
        }

        if (Minecraft.getMinecraft().player == null)
        {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.getEntity();

        if (player.getUniqueID().equals(Minecraft.getMinecraft().player.getUniqueID()))
        {
            if (!recognizedPlayers.contains(player.getUniqueID()))
            {
                if (Configuration.isCleanOnJoin())
                {
                    MemoryManager.cleanMemory(player);
                }

                lastCleanTime = System.currentTimeMillis();
                recognizedPlayers.add(player.getUniqueID());
            }
        }
    }
}
