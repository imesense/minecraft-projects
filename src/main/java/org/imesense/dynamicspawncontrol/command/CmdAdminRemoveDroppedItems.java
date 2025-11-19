package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;

import javax.annotation.Nonnull;

// Консольная команда на удаление всех валяющихся предметов
@InitLog
public final class CmdAdminRemoveDroppedItems extends CommandBase
{
    public CmdAdminRemoveDroppedItems()
    {

    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public String getName()
    {
        return "";
    }

    /**
     *
     * @param iCommandSender
     * @return
     */
    @Nonnull
    @Override
    public String getUsage(ICommandSender iCommandSender)
    {
        return "";
    }

    /**
     *
     * @param minecraftServer
     * @param iCommandSender
     * @param args
     * @throws CommandException
     */
    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, @Nonnull String... args) throws CommandException
    {

    }
}
