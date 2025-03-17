package org.imesense.dynamicspawncontrol.command;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;
import org.imesense.dynamicspawncontrol.core.memory.Configuration;
import org.imesense.dynamicspawncontrol.core.memory.MemoryEvents;
import org.imesense.dynamicspawncontrol.core.memory.MemoryManager;

import java.util.ArrayList;
import java.util.List;

public class CmdClientCleanMemory extends CommandBase implements IClientCommand
{
    public static final String NAME = "cleanmemory";

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        MemoryManager.cleanMemory(sender);
        MemoryEvents.lastCleanTime = System.currentTimeMillis();
    }

    @MethodsReturnNonnullByDefault
    public String getName()
    {
        return "cleanmemory";
    }

    @MethodsReturnNonnullByDefault
    public List<String> getAliases()
    {
        List<String> aliases = new ArrayList();
        String[] var2 = Configuration.commandAliases.split("\\s+");
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4)
        {
            String string = var2[var4];

            if (!string.isEmpty())
            {
                aliases.add(string);
            }
        }

        return aliases;
    }

    @MethodsReturnNonnullByDefault
    public String getUsage(ICommandSender sender)
    {
        return "/cleanmemory";
    }

    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message)
    {
        return false;
    }
}
