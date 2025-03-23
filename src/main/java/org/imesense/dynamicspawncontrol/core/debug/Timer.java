package org.imesense.dynamicspawncontrol.core.debug;

import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@InitLog
public final class Timer
{
    private long startTime;

    public Timer()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void start()
    {
        this.startTime = System.nanoTime();
    }

    public double stop()
    {
        long endTime = System.nanoTime();
        return (endTime - this.startTime) / 1_000_000.00;
    }
}
