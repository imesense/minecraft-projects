package org.imesense.dynamicspawncontrol.core.taskmanager;

public final class LogTask extends Task<Void>
{
    private final Runnable logAction;

    public LogTask(Runnable logAction)
    {
        super("LogTask", TaskManager.TaskPriority.LOW);
        this.logAction = logAction;
    }

    @Override
    public Void execute()
    {
        logAction.run();
        return null;
    }
}
