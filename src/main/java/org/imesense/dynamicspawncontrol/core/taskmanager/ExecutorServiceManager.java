package org.imesense.dynamicspawncontrol.core.taskmanager;

import lombok.Getter;

import java.util.concurrent.*;

public final class ExecutorServiceManager
{
    @Getter
    private final ScheduledExecutorService scheduler;
    private final ExecutorService workerPool;
    private final ExecutorService ioPool;
    private final ExecutorService logPool;

    public ExecutorServiceManager()
    {
        this.scheduler = Executors.newScheduledThreadPool(1,
                new NamedThreadFactory("DSC-Scheduler"));
        this.workerPool = Executors.newFixedThreadPool(4,
                new NamedThreadFactory("DSC-Worker"));
        this.ioPool = Executors.newCachedThreadPool(
                new NamedThreadFactory("DSC-IO"));
        this.logPool = Executors.newSingleThreadExecutor(
                new NamedThreadFactory("DSC-Logger"));
    }

    public ExecutorService selectExecutor(Task<?> task)
    {
        if (task instanceof LogTask)
        {
            return logPool;
        }
        else
        {
            return workerPool;
        }
    }

    public void shutdown(long timeoutMs)
    {
        shutdownExecutor(scheduler, "Scheduler", 2000);
        shutdownExecutor(workerPool, "WorkerPool", 3000);
        shutdownExecutor(ioPool, "IOPool", 3000);
        shutdownExecutor(logPool, "LoggerPool", 3000);
    }

    private void shutdownExecutor(ExecutorService executor, String name, long timeoutMs)
    {
        executor.shutdown();

        try
        {
            if (!executor.awaitTermination(timeoutMs, TimeUnit.MILLISECONDS))
            {
                System.out.println("[TaskManager] " + name + " didn't terminate, forcing shutdown...");
                executor.shutdownNow();
            }
        }
        catch (InterruptedException interruptedException)
        {
            System.out.println("[TaskManager] " + name + " shutdown interrupted");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public TaskManager.TaskManagerStats getStats()
    {
        return new TaskManager.TaskManagerStats(
                0,
                ((ThreadPoolExecutor) workerPool).getActiveCount(),
                ((ThreadPoolExecutor) ioPool).getActiveCount(),
                ((ThreadPoolExecutor) logPool).getActiveCount(),
                ((ScheduledThreadPoolExecutor) scheduler).getQueue().size()
        );
    }
}
