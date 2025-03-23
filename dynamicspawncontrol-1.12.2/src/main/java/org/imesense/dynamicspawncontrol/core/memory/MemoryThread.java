package org.imesense.dynamicspawncontrol.core.memory;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.TextComponentHelper;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

public final class MemoryThread implements Runnable
{
    private ICommandSender sender = null;

    public MemoryThread(ICommandSender sender) {
        this.sender = sender;
    }

    public MemoryThread()
    {

    }

    public void run()
    {
        Log.writeDataToLogFile(0, "Memory cleaner thread started!");

        if (this.sender != null && Configuration.showMessage)
        {
            this.sender.sendMessage(TextComponentHelper.createComponentTranslation(this.sender, "memorycleaner.gc.start", new Object[0]));
        }

        System.gc();

        try
        {
            Thread.sleep(1000L);
        } catch (InterruptedException ignored) { }

        System.gc();

        if (this.sender != null && Configuration.showMessage)
        {
            this.sender.sendMessage(TextComponentHelper.createComponentTranslation(this.sender, "memorycleaner.gc.end", new Object[0]));
        }

        Log.writeDataToLogFile(0, "Memory cleaner thread finished!");
    }
}