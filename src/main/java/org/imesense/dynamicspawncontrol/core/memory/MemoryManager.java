package org.imesense.dynamicspawncontrol.core.memory;

import net.minecraft.command.ICommandSender;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class MemoryManager
{
    private static final ScheduledExecutorService executor =
    Executors.newSingleThreadScheduledExecutor(r ->
    {
        Thread thread = new Thread(r, "[DynamicSpawnControl]: MemoryCleaner GC Thread");
        thread.setDaemon(true);

        return thread;
    });

    public static void cleanMemory(ICommandSender sender)
    {
        executor.execute(new MemoryThread(sender));
    }

    public static void cleanMemory()
    {
        executor.execute(new MemoryThread());
    }
}
