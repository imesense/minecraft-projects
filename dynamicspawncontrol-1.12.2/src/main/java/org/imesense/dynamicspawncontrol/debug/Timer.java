package org.imesense.dynamicspawncontrol.debug;

import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public final class Timer
{
    /**
     *
     */
    private long startTime;

    /**
     *
     * @param nameClass
     */
    public Timer(final String nameClass)
    {
        Log.writeDataToLogFile(0, nameClass);
    }

    /**
     *
     */
    public void start()
    {
        this.startTime = System.nanoTime();
    }

    /**
     *
     * @return
     */
    public double stop()
    {
        long endTime = System.nanoTime();
        return (endTime - this.startTime) / 1_000_000.0;
    }
}
