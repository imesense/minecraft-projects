package org.imesense.dynamicspawncontrol.core.debug;

import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

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
     */
    public Timer()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
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
        return (endTime - this.startTime) / 1_000_000.00;
    }
}
