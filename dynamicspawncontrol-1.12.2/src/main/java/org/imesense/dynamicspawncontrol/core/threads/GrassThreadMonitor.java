package org.imesense.dynamicspawncontrol.core.threads;

public final class GrassThreadMonitor
{
    private static GrassThreadMonitor instance;
    private final GrassThreadStats stats = new GrassThreadStats();

    public static class GrassThreadStats
    {
        public int queueSize;
        public int activeTasks;
        public double avgProcessTime;
        public double maxProcessTime;
        public int totalProcessed;
        public int errorCount;
        private long totalProcessTime;

        public GrassThreadStats()
        {

        }

        public GrassThreadStats(GrassThreadStats other)
        {
            this.queueSize = other.queueSize;
            this.activeTasks = other.activeTasks;
            this.avgProcessTime = other.avgProcessTime;
            this.maxProcessTime = other.maxProcessTime;
            this.totalProcessed = other.totalProcessed;
            this.errorCount = other.errorCount;
            this.totalProcessTime = other.totalProcessTime;
        }
    }

    public static synchronized GrassThreadMonitor getInstance()
    {
        if (instance == null)
        {
            instance = new GrassThreadMonitor();
        }

        return instance;
    }

    public void recordTaskStart(int queueSize)
    {
        synchronized (stats)
        {
            stats.queueSize = queueSize;
            stats.activeTasks++;
        }
    }

    public void recordTaskCompletion(long duration)
    {
        synchronized (stats)
        {
            stats.activeTasks--;
            stats.totalProcessed++;
            stats.totalProcessTime += duration;
            stats.avgProcessTime = (double) stats.totalProcessTime / stats.totalProcessed;
            stats.maxProcessTime = Math.max(stats.maxProcessTime, duration);
        }
    }

    public void recordTaskEnd()
    {
        synchronized (stats)
        {
            stats.activeTasks = 0;
        }
    }

    public void recordError()
    {
        synchronized (stats)
        {
            stats.errorCount++;
        }
    }

    public GrassThreadStats getStats()
    {
        synchronized (stats)
        {
            return new GrassThreadStats(stats);
        }
    }
}
