package org.imesense.dynamicspawncontrol.command.gameplay;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.imesense.dynamicspawncontrol.command.base.AnnotatedCommand;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.annotation.cmdconsole.CommandInfo;
import org.imesense.dynamicspawncontrol.core.annotation.cmdconsole.CommandSide;
import org.imesense.dynamicspawncontrol.core.annotation.cmdconsole.RequiredPermission;

import javax.annotation.Nonnull;

@CommandInfo(
        description = "Показывает seed текущего мира",
        usage = "/new_dsc_seed",
        aliases = {"worldseed", "seedinfo"}
)
@CommandSide(CommandSide.Side.BOTH)
@RequiredPermission(value = 0, playerOnly = true)
@TODO(value = "In development. A completely new concept of using console commands to separate the client and server", showOnce = false, priority = TODO.TodoPriority.HIGH)
public class WorldSeedCommand extends AnnotatedCommand
{
    @Nonnull
    @Override
    public String getName()
    {
        return "new_dsc_seed";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
    {
        EntityPlayer player = requirePlayer(sender);
        long seed = server.getWorld(0).getSeed();

        sendSuccess(sender, "Seed world: §6" + seed);
        sendInfo(sender, "World: §a" + player.world.provider.getDimensionType().getName());
    }
}
