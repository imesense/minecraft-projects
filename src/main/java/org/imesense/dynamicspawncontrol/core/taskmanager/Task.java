package org.imesense.dynamicspawncontrol.core.taskmanager;

import lombok.Getter;
import java.util.concurrent.CompletableFuture;

@Getter
public abstract class Task<T>
{
    private final String name;
    private final TaskManager.TaskPriority priority;
    private final CompletableFuture<T> future;

    public Task(String name, TaskManager.TaskPriority priority)
    {
        this.name = name;
        this.priority = priority;
        this.future = new CompletableFuture<>();
    }

    public abstract T execute() throws Exception;

    void complete(T result)
    {
        future.complete(result);
    }

    void completeExceptionally(Throwable throwable)
    {
        future.completeExceptionally(throwable);
    }
}
