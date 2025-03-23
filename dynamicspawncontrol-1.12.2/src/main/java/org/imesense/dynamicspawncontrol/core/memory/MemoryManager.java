package org.imesense.dynamicspawncontrol.core.memory;

import net.minecraft.command.ICommandSender;

public final class MemoryManager
{
    public static void cleanMemory(ICommandSender sender)
    {
        Runnable runnable = new MemoryThread(sender);
        Thread gcThread = new Thread(runnable, "MemoryCleaner GC Thread");
        gcThread.setDaemon(true);
        gcThread.start();
    }

    public static void cleanMemory()
    {
        Runnable runnable = new MemoryThread();
        Thread gcThread = new Thread(runnable, "MemoryCleaner GC Thread");
        gcThread.setDaemon(true);
        gcThread.start();
    }
}
