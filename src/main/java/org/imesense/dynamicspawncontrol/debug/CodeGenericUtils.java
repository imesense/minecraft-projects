package org.imesense.dynamicspawncontrol.debug;

import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

/**
 *
 */
public class CodeGenericUtils
{
    /**
     *
     * @param object
     * @param message
     * @return
     * @param <T>
     */
    public static <T> T checkObjectNotNull(final T object, final String message)
    {
        if (object == null)
        {
            Log.writeDataToLogFile(Log.TypeLog[2], "Object<T> return null: " + message);
            throw new NullPointerException(message);
        }

        return object;
    }
}
