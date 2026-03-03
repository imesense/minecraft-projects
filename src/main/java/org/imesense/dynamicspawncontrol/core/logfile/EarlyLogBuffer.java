package org.imesense.dynamicspawncontrol.core.logfile;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class EarlyLogBuffer
{
    private static final Queue<Runnable> BUFFER = new ConcurrentLinkedQueue<>();

    public static void log(int level, String message)
    {
        if (LogIsReady.isReady())
        {
            Logger.write(level, message);
        }
        else
        {
            BUFFER.add(() -> Logger.write(level, message));
        }
    }

    public static void flush()
    {
        Runnable runnable;

        while ((runnable = BUFFER.poll()) != null)
        {
            runnable.run();
        }
    }
}
