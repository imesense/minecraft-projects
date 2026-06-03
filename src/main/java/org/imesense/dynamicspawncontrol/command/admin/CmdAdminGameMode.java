package org.imesense.dynamicspawncontrol.command.admin;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.text.ChatColorUtil;

import javax.annotation.Nonnull;

@InitLog
public final class CmdAdminGameMode extends CommandBase
{
    public CmdAdminGameMode()
    {

    }

    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_gm";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_gm";
    }

    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer, @Nonnull ICommandSender iCommandSender, @Nonnull String... args)
    {
        if (args.length != 1)
        {
            iCommandSender.sendMessage(new TextComponentString(getUsage(iCommandSender)));
            return;
        }

        int mode;

        try
        {
            mode = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException exception)
        {
            LogManager.error(String.format("Error parsing game mode: %s. Exception: %s", args[0], exception.getMessage()));

            ChatColorUtil.sendColoredMessage(
                    iCommandSender instanceof EntityPlayer ? (EntityPlayer) iCommandSender : null,
                    "Invalid game mode: " + args[0],
                    TextFormatting.RED
            );


            return;
        }

        if (iCommandSender instanceof EntityPlayerMP)
        {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) iCommandSender;
            entityPlayerMP.setGameType(GameType.getByID(mode));
        }
    }
}
