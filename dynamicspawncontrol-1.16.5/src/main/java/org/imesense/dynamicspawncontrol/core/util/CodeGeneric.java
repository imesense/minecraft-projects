package org.imesense.dynamicspawncontrol.core.util;

import org.imesense.dynamicspawncontrol.core.LogFile;

/**
 *
 */
public final class CodeGeneric
{
    /**
     *
     * @param _CLASS
     * @param <T>
     */
    public static <T> void printInitClassToLog(final Class<T> _CLASS)
    {
        LogFile.writeDataToLogFile(3,
                String.format("Initializing a class: {%s}", _CLASS.getName()));
    }
}
