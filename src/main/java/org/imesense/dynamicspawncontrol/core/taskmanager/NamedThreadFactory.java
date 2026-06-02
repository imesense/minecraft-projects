package org.imesense.dynamicspawncontrol.core.taskmanager;

import lombok.NonNull;
import java.util.concurrent.ThreadFactory;

public final class NamedThreadFactory implements ThreadFactory
{
    private int count = 0;
    private final String name;

    public NamedThreadFactory(String name)
    {
        this.name = name;
    }

    @Override
    public Thread newThread(@NonNull Runnable runnable)
    {
        Thread thread = new Thread(runnable, name + "-" + count++);
        thread.setDaemon(true);
        return thread;
    }
}
