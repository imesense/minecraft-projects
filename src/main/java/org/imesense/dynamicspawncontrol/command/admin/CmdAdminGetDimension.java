package org.imesense.dynamicspawncontrol.command.admin;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.text.ChatColorUtil;
import org.imesense.dynamicspawncontrol.core.text.CmdCallType;

import javax.annotation.Nonnull;

@InitLog
public final class CmdAdminGetDimension extends CommandBase
{
    public CmdAdminGetDimension()
    {

    }

    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_gd";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_gd";
    }

    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer, @Nonnull ICommandSender iCommandSender, @Nonnull String... args)
    {
        if (args.length > 0)
        {
            iCommandSender.sendMessage(new TextComponentString(
                    ChatColorUtil.color(CmdCallType.COMMAND + " The command does not accept arguments",
                            TextFormatting.RED)));
            return;
        }

        if (iCommandSender instanceof EntityPlayerMP)
        {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) iCommandSender;

            int worldId = entityPlayerMP.world.provider.getDimension();
            entityPlayerMP.sendMessage(new TextComponentString("World ID: " + worldId));
        }
    }
}
