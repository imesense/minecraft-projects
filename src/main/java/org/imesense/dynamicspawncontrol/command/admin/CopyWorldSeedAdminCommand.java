package org.imesense.dynamicspawncontrol.command.admin;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.text.ChatColorUtil;

import javax.annotation.Nonnull;

@InitLog
public final class CopyWorldSeedAdminCommand extends CommandBase
{
    public CopyWorldSeedAdminCommand()
    {

    }

    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_copy_world_seed";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_copy_world_seed";
    }

    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer, @Nonnull ICommandSender iCommandSender, @Nonnull String... args) throws CommandException
    {
        long seed = minecraftServer.getWorld(0).getSeed();

        LogManager.info("Get World Seed: " + seed);

        ChatColorUtil.sendColoredMessage(
                iCommandSender instanceof EntityPlayerMP ? (EntityPlayer) iCommandSender : null,
                "Get World Seed: " + seed,
                TextFormatting.GREEN);
    }
}
