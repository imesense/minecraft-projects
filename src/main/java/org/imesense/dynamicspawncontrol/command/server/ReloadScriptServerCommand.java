package org.imesense.dynamicspawncontrol.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.register.parser.ParserRegister;
import org.imesense.dynamicspawncontrol.core.text.ChatColorUtil;
import org.imesense.dynamicspawncontrol.core.text.CmdCallType;

import javax.annotation.Nonnull;

@InitLog
public final class ReloadScriptServerCommand extends CommandBase
{
    public ReloadScriptServerCommand()
    {

    }

    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_reload_scripts";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_reload_scripts";
    }

    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer, @Nonnull ICommandSender iCommandSender, @Nonnull String... args)
    {
        if (args.length > 0)
        {
            iCommandSender.sendMessage(new TextComponentString(
                    ChatColorUtil.color(CmdCallType.COMMAND + " The command does not accept arguments",
                            TextFormatting.RED)
            ));
        }
        else
        {
            ParserRegister.getInstance().reloadAllConfigs();

            iCommandSender.sendMessage(new TextComponentString(
                    ChatColorUtil.color(CmdCallType.COMMAND + " Configurations have been reloaded",
                            TextFormatting.GREEN)
            ));
        }
    }
}
