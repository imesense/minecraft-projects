package org.imesense.dynamicspawncontrol.core.taskmanager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public final class TaskProcessor
{
    private final TaskManager taskManager;
    private final BlockingQueue<Task<?>> taskQueue;
    private final Thread taskProcessorThread;
    private volatile boolean running;

    public TaskProcessor(TaskManager taskManager, BlockingQueue<Task<?>> taskQueue)
    {
        this.taskManager = taskManager;
        this.taskQueue = taskQueue;
        this.running = true;
        this.taskProcessorThread = new Thread(new ProcessorRunnable(), "DSC-TaskProcessor");
        this.taskProcessorThread.setDaemon(true);
        this.taskProcessorThread.start();
    }

    public void shutdown()
    {
        running = false;
        taskProcessorThread.interrupt();
    }

    public void join(long timeout) throws InterruptedException
    {
        taskProcessorThread.join(timeout);
    }

    public void processRemainingTasks()
    {
        Task<?> task;

        while ((task = taskQueue.poll()) != null)
        {
            taskManager.processTask(task);
        }
    }

    private class ProcessorRunnable implements Runnable
    {
        @Override
        public void run()
        {
            while (running)
            {
                try
                {
                    Task<?> task = taskQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (task != null)
                    {
                        taskManager.processTask(task);
                    }
                    processRemainingTasks();
                }
                catch (InterruptedException interruptedException)
                {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            processRemainingTasks();
        }
    }
}
