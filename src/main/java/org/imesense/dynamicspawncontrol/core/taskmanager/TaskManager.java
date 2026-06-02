package org.imesense.dynamicspawncontrol.core.taskmanager;

import lombok.Getter;
import java.util.concurrent.*;
import java.util.function.Consumer;

public final class TaskManager
{
    private static TaskManager instance;

    private final ExecutorServiceManager executorManager;
    private final ErrorHandlerManager errorHandlerManager;
    private final BlockingQueue<Task<?>> taskQueue;
    private final TaskProcessor taskProcessor;

    @Getter
    private volatile boolean shuttingDown = false;
    private volatile boolean running;

    private static final int QUEUE_CAPACITY = 1000;

    public enum TaskPriority
    {
        LOW, NORMAL, HIGH, CRITICAL
    }

    private TaskManager()
    {
        this.executorManager = new ExecutorServiceManager();
        this.errorHandlerManager = new ErrorHandlerManager();
        this.taskQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.running = true;
        this.taskProcessor = new TaskProcessor(this, taskQueue);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public static synchronized TaskManager getInstance()
    {
        if (instance == null)
        {
            instance = new TaskManager();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    <T> void processTask(Task<T> task)
    {
        ExecutorService executor = executorManager.selectExecutor(task);
        executor.submit(() -> {
            try
            {
                T result = task.execute();
                task.complete(result);
            }
            catch (Exception exception)
            {
                task.completeExceptionally(exception);
                errorHandlerManager.notifyError(exception);
            }
        });
    }

    public <T> CompletableFuture<T> submitTask(Task<T> task)
    {
        if (!running)
        {
            CompletableFuture<T> failed = new CompletableFuture<>();
            failed.completeExceptionally(new IllegalStateException("TaskManager is shutting down"));
            return failed;
        }

        try
        {
            taskQueue.offer(task, 100, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException interruptedException)
        {
            Thread.currentThread().interrupt();
            CompletableFuture<T> failed = new CompletableFuture<>();
            failed.completeExceptionally(interruptedException);
            return failed;
        }

        return task.getFuture();
    }

    public CompletableFuture<Void> submitLogTask(Runnable logAction)
    {
        return submitTask(new LogTask(logAction));
    }

    public ScheduledFuture<?> scheduleTask(Runnable task, long delay, TimeUnit unit)
    {
        return executorManager.getScheduler().schedule(() ->
                submitTask(new Task<Void>("ScheduledTask", TaskPriority.NORMAL)
                {
                    @Override
                    public Void execute()
                    {
                        task.run();
                        return null;
                    }
                }), delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay,
                                                  long period, TimeUnit unit)
    {
        return executorManager.getScheduler().scheduleAtFixedRate(() ->
                submitTask(new Task<Void>("PeriodicTask", TaskPriority.NORMAL)
                {
                    @Override
                    public Void execute()
                    {
                        task.run();
                        return null;
                    }
                }), initialDelay, period, unit);
    }

    public void addErrorHandler(Consumer<Throwable> handler)
    {
        errorHandlerManager.addHandler(handler);
    }

    public TaskManagerStats getStats()
    {
        TaskManagerStats stats = executorManager.getStats();
        return new TaskManagerStats(
                taskQueue.size(),
                stats.activeWorkers,
                stats.activeIoThreads,
                stats.scheduledTasks
        );
    }

    public static class TaskManagerStats
    {
        public final int queueSize;
        public final int activeWorkers;
        public final int activeIoThreads;
        public final int scheduledTasks;

        public TaskManagerStats(int queueSize, int activeWorkers,
                                int activeIoThreads, int scheduledTasks)
        {
            this.queueSize = queueSize;
            this.activeWorkers = activeWorkers;
            this.activeIoThreads = activeIoThreads;
            this.scheduledTasks = scheduledTasks;
        }
    }

    public void shutdown()
    {
        if (shuttingDown)
            return;

        shuttingDown = true;
        running = false;

        System.out.println("[TaskManager] Starting graceful shutdown...");

        taskProcessor.shutdown();
        try
        {
            taskProcessor.join(2000);
        }
        catch (InterruptedException ignored) { }

        processRemainingTasksWithTimeout(5000);
        executorManager.shutdown(5000);

        System.out.println("[TaskManager] Shutdown complete");
    }

    private void processRemainingTasksWithTimeout(long timeoutMs)
    {
        long startTime = System.currentTimeMillis();
        int processedCount = 0;

        while (!taskQueue.isEmpty() && (System.currentTimeMillis() - startTime) < timeoutMs)
        {
            Task<?> task = taskQueue.poll();
            if (task != null)
            {
                try
                {
                    processTask(task);
                    processedCount++;
                }
                catch (Exception exception)
                {
                    errorHandlerManager.notifyError(exception);
                }
            }
        }

        System.out.println("[TaskManager] Processed " + processedCount + " remaining tasks");

        int remaining = taskQueue.size();
        if (remaining > 0)
        {
            System.out.println("[TaskManager] Warning: " + remaining + " tasks were dropped");
            taskQueue.clear();
        }
    }
}
