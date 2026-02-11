package org.imesense.dynamicspawncontrol.core.logfile;

public final class LogIsReady
{
    private static volatile boolean READY = false;

    public static boolean isReady()
    {
        return READY;
    }

    public static void markReady()
    {
        READY = true;
    }
}
