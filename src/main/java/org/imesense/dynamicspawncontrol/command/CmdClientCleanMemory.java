package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.memory.Configuration;
import org.imesense.dynamicspawncontrol.core.memory.MemoryEvents;
import org.imesense.dynamicspawncontrol.core.memory.MemoryManager;

import javax.annotation.Nonnull;
import java.util.List;

/**
 *
 */
@InitLog
public final class CmdClientCleanMemory extends CommandBase
{
    /**
     *
     */
    public CmdClientCleanMemory()
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
        return "dsc_clean_up_memory";
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
        return "/dsc_clean_up_memory";
    }

    /**
     *
     * @param minecraftServer
     * @param iCommandSender
     * @param args
     * @throws CommandException
     */
    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer,
                        @Nonnull ICommandSender iCommandSender, @Nonnull String... args) throws CommandException
    {
        MemoryManager.cleanMemory(iCommandSender);
        MemoryEvents.setLastCleanTime(System.currentTimeMillis());
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public List<String> getAliases()
    {
        return Configuration.getCommandAliases();
    }
}
