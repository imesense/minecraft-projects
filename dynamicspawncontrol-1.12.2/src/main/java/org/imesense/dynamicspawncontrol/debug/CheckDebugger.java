package org.imesense.dynamicspawncontrol.debug;

public final class CheckDebugger
{
    public boolean IsRunDebugger;

    private static CheckDebugger instance = null;

    public CheckDebugger()
    {
        instance = this;
        IsRunDebugger = run();
    }

    public static CheckDebugger getInstance()
    {
        return instance;
    }

    private boolean run()
    {
        return (System.getProperty("java.class.path").toLowerCase().contains("idea_rt.jar"));
    }
}
