package org.imesense.dynamicspawncontrol.command.develop;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.imesense.dynamicspawncontrol.command.AnnotatedCommand;
import org.imesense.dynamicspawncontrol.core.annotation.cmdconsole.CommandInfo;
import org.imesense.dynamicspawncontrol.core.annotation.cmdconsole.CommandSide;
import org.imesense.dynamicspawncontrol.core.annotation.cmdconsole.RequiredPermission;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import javax.annotation.Nonnull;

@CommandInfo(
        description = "Логирует все зарегистрированные инстансы классов",
        usage = "/dsc_instances",
        aliases = {"instances", "loginstances"}
)
@CommandSide(CommandSide.Side.BOTH)
@RequiredPermission(value = 0, playerOnly = true)
public final class GetCollectionClassesInstanceCommand extends AnnotatedCommand
{
    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_instances";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
    {
        this.requirePermission(sender, 2);

        CodeGeneric.logAllInstances();

        sendSuccess(sender, "All instances have been logged to the log file!");
        sendInfo(sender, "Please check the log file in the logs/ directory");
    }
}