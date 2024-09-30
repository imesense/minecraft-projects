package org.imesense.dynamicspawncontrol.debug;

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
        CodeGenericUtils.printInitClassToLog(this.getClass());
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
