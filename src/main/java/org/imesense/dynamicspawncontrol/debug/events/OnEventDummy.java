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
        Log.writeDataToLogFile(0, nameClass);
    }
}
