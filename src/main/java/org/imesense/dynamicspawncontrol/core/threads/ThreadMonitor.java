package org.imesense.dynamicspawncontrol.core.threads;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class ThreadMonitor
{
    private static ThreadMonitor instance;
    private final Map<String, ThreadStats> threadStats = new ConcurrentHashMap<>();

    public static class ThreadStats
    {
        public final String threadName;
        public double currentDelay;
        public double maxDelay;
        public double averageDelay;
        public long lastUpdate;
        public int tickCount;
        public long totalDelay;

        public ThreadStats(String name)
        {
            this.threadName = name;
        }
    }

    private ThreadMonitor()
    {
        registerThread("main");
    }

    public static synchronized ThreadMonitor getInstance()
    {
        if (instance == null)
        {
            instance = new ThreadMonitor();
        }

        return instance;
    }

    public void registerThread(String threadName)
    {
        threadStats.putIfAbsent(threadName, new ThreadStats(threadName));
    }

    public void updateThreadStats(String threadName, long expectedInterval, long actualInterval)
    {
        ThreadStats stats = threadStats.get(threadName);

        if (stats != null)
        {
            long currentTime = System.currentTimeMillis();
            long delay = Math.max(0, actualInterval - expectedInterval);

            synchronized(stats)
            {
                stats.currentDelay = delay;
                stats.maxDelay = Math.max(stats.maxDelay, delay);
                stats.totalDelay += delay;
                stats.tickCount++;
                stats.averageDelay = (double) stats.totalDelay / stats.tickCount;
                stats.lastUpdate = currentTime;
            }
        }
    }

    public ThreadStats getThreadStats(String threadName)
    {
        return threadStats.get(threadName);
    }

    public void updateMainThreadStats()
    {
        long currentTime = System.currentTimeMillis();
        ThreadStats mainStats = threadStats.get("main");

        if (mainStats != null)
        {
            long timeSinceLastUpdate = currentTime - mainStats.lastUpdate;
            updateThreadStats("main", 50, timeSinceLastUpdate);
        }
    }

    public void startLoggingThreadMonitoring()
    {
        registerThread("logging");
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() ->
        {
            updateThreadStats("logging", 1000, 1000);
        }, 0, 1, TimeUnit.SECONDS);
    }
}
