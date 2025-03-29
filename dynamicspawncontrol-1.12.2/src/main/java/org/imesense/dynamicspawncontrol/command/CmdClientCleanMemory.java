package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.memory.Configuration;
import org.imesense.dynamicspawncontrol.core.memory.MemoryEvents;
import org.imesense.dynamicspawncontrol.core.memory.MemoryManager;

import java.util.List;

@InitLog
public final class CmdClientCleanMemory extends CommandBase
{
    @Override
    public String getName()
    {
        return "dsc_clean_up_memory";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender)
    {
        return "/dsc_clean_up_memory";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String... args) throws CommandException
    {
        MemoryManager.cleanMemory(iCommandSender);
        MemoryEvents.setLastCleanTime(System.currentTimeMillis());
    }

    @Override
    public List<String> getAliases()
    {
        return Configuration.getCommandAliases();
    }
}
