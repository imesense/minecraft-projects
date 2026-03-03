package org.imesense.dynamicspawncontrol.core.memory;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;

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
            LogManager.info("[MemoryCleaner] Starting memory cleanup...");

            Runtime runtime = Runtime.getRuntime();
            long beforeMem = runtime.totalMemory() - runtime.freeMemory();

            if (sender != null && Configuration.isShowMessage())
            {
                TextComponentString msg =
                        new TextComponentString("Starting memory cleanup...");
                msg.getStyle().setColor(TextFormatting.GREEN);
                sender.sendMessage(msg);
            }

            System.gc();

            long afterMem = runtime.totalMemory() - runtime.freeMemory();
            long freedMem = beforeMem - afterMem;

            if (sender != null && Configuration.isShowMessage())
            {
                TextComponentString prefix =
                        new TextComponentString("Memory cleanup complete! Freed ");
                prefix.getStyle().setColor(TextFormatting.GREEN);

                TextComponentString value =
                        new TextComponentString(String.valueOf(freedMem / (1024 * 1024)));
                value.getStyle().setColor(TextFormatting.YELLOW);

                TextComponentString suffix =
                        new TextComponentString(" MB.");
                suffix.getStyle().setColor(TextFormatting.GREEN);

                prefix.appendSibling(value);
                prefix.appendSibling(suffix);

                sender.sendMessage(prefix);
            }

            String logMessage = "[MemoryCleaner] Cleanup complete! Freed " + freedMem + " bytes";

            LogManager.info(logMessage);
        }
        catch (Exception exception)
        {
            String error = "[MemoryCleaner] Error during memory cleanup: " + exception.getMessage();

            LogManager.error(error);

            if (sender != null && Configuration.isShowMessage())
            {
                TextComponentString msg = new TextComponentString("Memory cleanup failed!");

                msg.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(msg);
            }
        }
    }
}
