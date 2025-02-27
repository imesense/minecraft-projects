package org.imesense.dynamicspawncontrol.core.util;

import com.google.gson.JsonElement;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.List;
import java.util.function.Function;

/**
 *
 */
public final class CodeGeneric
{
    /**
     *
     * @param clazz
     * @return
     */
    public static boolean hasDefaultConstructor(Class<?> clazz)
    {
        try
        {
            clazz.getConstructor();
            return true;
        }
        catch (NoSuchMethodException exception)
        {
            return false;
        }
    }

    /**
     *
     * @param _CLASS
     * @param <T>
     */
    public static <T> void printInitClassToLog(final Class<T> _CLASS)
    {
        Log.writeDataToLogFile(3,
                String.format("Initializing a class: {%s}", _CLASS.getName()));
    }

    /**
     *
     * @param object
     * @param _class
     * @return
     * @param <T>
     */
    public static <T> T as(Object object, Class<T> _class)
    {
        return _class.isInstance(object) ?
                _class.cast(object) : null;
    }

    /**
     *
     * @param message
     */
    public static void logAndThrow(String message)
    {
        Log.writeDataToLogFile(2, message);
        throw new RuntimeException(message);
    }

    /**
     *
     * @param message
     * @param exception
     */
    public static void logAndThrow(String message, Exception exception)
    {
        Log.writeDataToLogFile(2, message);
        throw new RuntimeException(exception);
    }
}
