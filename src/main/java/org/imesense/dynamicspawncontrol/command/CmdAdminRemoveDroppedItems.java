package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;

import javax.annotation.Nonnull;

/**
 *
 */
@InitLog
@TODO(value = "[WIP] Консольная команда для удаление всех валяющихся предметов", showOnce = false, priority = TODO.TodoPriority.HIGH)
public final class CmdAdminRemoveDroppedItems extends CommandBase
{
    /**
     *
     */
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
        return "dsc_remove_dropped_items";
    }

    /**
     *
     * @param iCommandSender
     * @return
     */
    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_remove_dropped_items";
    }

    /**
     *
     * @param minecraftServer
     * @param iCommandSender
     * @param args
     * @throws CommandException
     */
    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer, @Nonnull ICommandSender iCommandSender, @Nonnull String... args) throws CommandException
    {

    }
}
