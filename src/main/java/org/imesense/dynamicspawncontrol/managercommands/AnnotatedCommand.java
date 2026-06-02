package org.imesense.dynamicspawncontrol.managercommands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.annotation.cmdconsole.CommandInfo;
import org.imesense.dynamicspawncontrol.core.annotation.cmdconsole.CommandSide;
import org.imesense.dynamicspawncontrol.core.annotation.cmdconsole.RequiredPermission;
import org.imesense.dynamicspawncontrol.core.text.ChatColorUtil;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

@TODO(value = "In development. A completely new concept of using console commands to separate the client and server", showOnce = false, priority = TODO.TodoPriority.HIGH)
public abstract class AnnotatedCommand extends CommandBase
{
    protected final CommandSide side;
    protected final CommandInfo info;
    protected final RequiredPermission permission;

    public AnnotatedCommand()
    {
        this.permission = this.getClass().getAnnotation(RequiredPermission.class);
        this.side = this.getClass().getAnnotation(CommandSide.class);
        this.info = this.getClass().getAnnotation(CommandInfo.class);
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender)
    {
        if (permission == null)
        {
            return true;
        }

        if (permission.playerOnly() && !(sender instanceof EntityPlayer))
        {
            return false;
        }

        if (permission.value() > 0)
        {
            return sender.canUseCommand(permission.value(), this.getName());
        }

        if (!permission.customPermission().isEmpty())
        {
            return true; // test
        }

        return true;
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender)
    {
        if (info != null && !info.usage().isEmpty())
        {
            return info.usage();
        }

        return "/" + getName();
    }

    public String getDescription()
    {
        if (info != null && !info.description().isEmpty())
        {
            return info.description();
        }

        return "Нет описания";
    }

    @Override
    @Nonnull
    public List<String> getAliases()
    {
        if (info != null && info.aliases().length > 0)
        {
            return Arrays.asList(info.aliases());
        }

        return super.getAliases();
    }

    protected void requirePermission(ICommandSender sender, int requiredLevel) throws CommandException
    {
        if (!sender.canUseCommand(requiredLevel, this.getName()))
        {
            throw new CommandException("§cТребуется уровень OP " + requiredLevel + " или выше!");
        }
    }

    protected EntityPlayer requirePlayer(ICommandSender sender) throws CommandException
    {
        if (!(sender instanceof EntityPlayer))
        {
            throw new CommandException("§cЭта команда доступна только игрокам!");
        }

        return (EntityPlayer) sender;
    }

    protected void sendMessage(ICommandSender sender, String message, TextFormatting color)
    {
        if (sender instanceof EntityPlayer)
        {
            ChatColorUtil.sendColoredMessage((EntityPlayer) sender, message, color);
        }
        else
        {
            sender.sendMessage(new TextComponentString(message));
        }
    }

    protected void sendSuccess(ICommandSender sender, String message)
    {
        sendMessage(sender, "§a✓ " + message, TextFormatting.GREEN);
    }

    protected void sendError(ICommandSender sender, String message)
    {
        sendMessage(sender, "§c✗ " + message, TextFormatting.RED);
    }

    protected void sendInfo(ICommandSender sender, String message)
    {
        sendMessage(sender, "§eℹ " + message, TextFormatting.YELLOW);
    }

    protected void sendWarning(ICommandSender sender, String message)
    {
        sendMessage(sender, "§6⚠ " + message, TextFormatting.GOLD);
    }
}
