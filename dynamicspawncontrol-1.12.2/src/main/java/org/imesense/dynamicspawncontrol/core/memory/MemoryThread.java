package org.imesense.dynamicspawncontrol.core.memory;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.TextComponentHelper;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

public final class MemoryThread implements Runnable
{
    private final ICommandSender sender;

    public MemoryThread(ICommandSender sender)
    {
        this.sender = sender;
    }

    public MemoryThread()
    {
        this(null);
    }

    @Override
    public void run()
    {
        try
        {
            Log.writeDataToLogFile(0, "[MemoryCleaner] Starting memory cleanup...");

            System.out.println("[MemoryCleaner] Starting memory cleanup...");

            Runtime runtime = Runtime.getRuntime();
            long beforeMem = runtime.totalMemory() - runtime.freeMemory();

            if (sender != null && Configuration.isShowMessage())
            {
                sender.sendMessage(new TextComponentString("§aStarting memory cleanup..."));
            }

            System.gc();
            System.runFinalization();

            long afterMem = runtime.totalMemory() - runtime.freeMemory();
            long freedMem = beforeMem - afterMem;

            if (sender != null && Configuration.isShowMessage())
            {
                String message = String.format("§aMemory cleanup complete! Freed §e%d MB§a.", freedMem / (1024 * 1024));
                sender.sendMessage(new TextComponentString(message));
            }

            String logMessage = String.format("[MemoryCleaner] Cleanup complete! Freed %d bytes", freedMem);
            Log.writeDataToLogFile(0, logMessage);
            System.out.println(logMessage);
        }
        catch (Exception exception)
        {
            String errorMessage = "[MemoryCleaner] Error during memory cleanup: " + exception.getMessage();

            Log.writeDataToLogFile(2, errorMessage);
            System.err.println(errorMessage);

            if (sender != null && Configuration.isShowMessage())
            {
                sender.sendMessage(new TextComponentString("§cMemory cleanup failed!"));
            }
        }
    }
}