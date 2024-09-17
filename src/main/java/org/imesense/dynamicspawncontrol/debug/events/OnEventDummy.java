package org.imesense.dynamicspawncontrol.debug.events;

import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public final class OnEventDummy
{
    /**
     *
     */
    public OnEventDummy(final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);
    }
}
