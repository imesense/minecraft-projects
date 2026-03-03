package org.imesense.dynamicspawncontrol.core.taskmanager;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class TaskManager
{
    private static final int AVAILABLE_CORES =
            Math.max(1, Runtime.getRuntime().availableProcessors() - 1);

    private static final TaskManager INSTANCE =
            new TaskManager(AVAILABLE_CORES, "AsyncWorker");

    private final ExecutorService executor;

    private TaskManager(int threadCount, String threadNamePrefix)
    {
        this.executor = new ThreadPoolExecutor(
                threadCount,
                threadCount,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new NamedThreadFactory(threadNamePrefix),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public static TaskManager get()
    {
        return INSTANCE;
    }

    public Future<?> submit(Runnable task)
    {
        return executor.submit(wrap(task));
    }

    public <T> Future<T> submit(Callable<T> task)
    {
        return executor.submit(wrap(task));
    }

    public void execute(Runnable task)
    {
        executor.execute(wrap(task));
    }

    public void shutdown()
    {
        executor.shutdown();
    }

    public void shutdownNow()
    {
        executor.shutdownNow();
    }

    private Runnable wrap(Runnable task)
    {
        return () ->
        {
            try
            {
                task.run();
            }
            catch (Throwable throwable)
            {
                throwable.printStackTrace();
            }
        };
    }

    private <T> Callable<T> wrap(Callable<T> task)
    {
        return () ->
        {
            try
            {
                return task.call();
            }
            catch (Throwable throwable)
            {
                throwable.printStackTrace();
                throw throwable;
            }
        };
    }

    private static class NamedThreadFactory implements ThreadFactory
    {
        private final String prefix;
        private final AtomicInteger counter = new AtomicInteger(0);

        NamedThreadFactory(String prefix)
        {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable runnable)
        {
            Thread thread = new Thread(runnable);

            thread.setName(prefix + "-" + counter.incrementAndGet());
            thread.setDaemon(true);

            return thread;
        }
    }
}
