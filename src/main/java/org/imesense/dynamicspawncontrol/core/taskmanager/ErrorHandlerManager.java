package org.imesense.dynamicspawncontrol.core.taskmanager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class ErrorHandlerManager
{
    private final List<Consumer<Throwable>> errorHandlers = new CopyOnWriteArrayList<>();

    public void addHandler(Consumer<Throwable> handler)
    {
        errorHandlers.add(handler);
    }

    public void notifyError(Throwable error)
    {
        for (Consumer<Throwable> handler : errorHandlers)
        {
            try
            {
                handler.accept(error);
            }
            catch (Exception ignored) { }
        }
    }
}
