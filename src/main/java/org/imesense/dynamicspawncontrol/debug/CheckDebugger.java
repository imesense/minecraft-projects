package org.imesense.dynamicspawncontrol.debug;

/**
 *
 */
public final class CheckDebugger
{
    /**
     *
     */
    public boolean IsRunDebugger;

    /**
     *
     */
    public static CheckDebugger instance;

    /**
     *
     */
    public CheckDebugger()
    {
        instance = this;
        IsRunDebugger = run();
    }

    /**
     *
     * @return
     */
    private boolean run()
    {
        return (System.getProperty("java.class.path").toLowerCase().contains("idea_rt.jar"));
    }
}
