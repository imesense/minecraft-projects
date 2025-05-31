package org.imesense.dynamicspawncontrol.core.memory;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
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
            Log.write(0, "[MemoryCleaner] Starting memory cleanup...");
            System.out.println("[MemoryCleaner] Starting memory cleanup...");

            Runtime runtime = Runtime.getRuntime();
            long beforeMem = runtime.totalMemory() - runtime.freeMemory();

            if (sender != null && Configuration.isShowMessage())
            {
                TextComponentString message = new TextComponentString("Starting memory cleanup...");
                message.getStyle().setColor(TextFormatting.GREEN);
                sender.sendMessage(message);
            }

            System.gc();
            System.runFinalization();

            long afterMem = runtime.totalMemory() - runtime.freeMemory();
            long freedMem = beforeMem - afterMem;

            if (sender != null && Configuration.isShowMessage())
            {
                TextComponentString prefix = new TextComponentString("Memory cleanup complete! Freed ");
                prefix.getStyle().setColor(TextFormatting.GREEN);

                TextComponentString freedPart = new TextComponentString(String.valueOf(freedMem / (1024 * 1024)));
                freedPart.getStyle().setColor(TextFormatting.YELLOW);

                TextComponentString suffix = new TextComponentString(" MB.");
                suffix.getStyle().setColor(TextFormatting.GREEN);

                prefix.appendSibling(freedPart);
                prefix.appendSibling(suffix);

                sender.sendMessage(prefix);
            }

            String logMessage = String.format("[MemoryCleaner] Cleanup complete! Freed %d bytes", freedMem);
            Log.write(0, logMessage);
            System.out.println(logMessage);
        }
        catch (Exception exception)
        {
            String errorMessage = "[MemoryCleaner] Error during memory cleanup: " + exception.getMessage();
            Log.write(2, errorMessage);
            System.err.println(errorMessage);

            if (sender != null && Configuration.isShowMessage())
            {
                TextComponentString message = new TextComponentString("Memory cleanup failed!");
                message.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(message);
            }
        }
    }
}