package org.imesense.dynamicspawncontrol.command.admin;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldProvider;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.text.ChatColorUtil;
import org.imesense.dynamicspawncontrol.core.text.CmdCallType;

import javax.annotation.Nonnull;

@InitLog
public final class CmdAdminGetWorldMoonPhase extends CommandBase
{
    public CmdAdminGetWorldMoonPhase()
    {

    }

    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_moon_phase";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_moon_phase";
    }

    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer, @Nonnull ICommandSender iCommandSender, @Nonnull String... args)
    {
        WorldProvider worldProvider = iCommandSender.getEntityWorld().provider;
        int moonPhase = worldProvider.getMoonPhase(iCommandSender.getEntityWorld().getWorldTime());

        iCommandSender.sendMessage(new TextComponentString(
                ChatColorUtil.color(CmdCallType.COMMAND + " -> The current phase of the moon: " + moonPhase,
                        TextFormatting.AQUA)
        ));
    }
}
