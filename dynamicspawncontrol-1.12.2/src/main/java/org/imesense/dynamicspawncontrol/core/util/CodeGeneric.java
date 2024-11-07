package org.imesense.dynamicspawncontrol.core.util;

import com.google.gson.JsonElement;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeKey;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;

import java.util.List;
import java.util.function.Function;

/**
 *
 */
public final class CodeGeneric
{
    /**
     *
     * @param attributeMap
     * @param attributeKey
     * @param min
     * @param max
     * @param logParameterName
     * @return
     * @param <T>
     */
    public static <T extends Number> T checkParameter(AttributeMap<?> attributeMap, AttributeKey<?> attributeKey, T min, T max, String logParameterName)
    {
        Object object = attributeMap.get(attributeKey);

        if (object == null)
        {
            Log.writeDataToLogFile(2,
                    "Parameter '" + logParameterName + "' missing or return null");

            throw new RuntimeException();
        }

        T numericValue;

        try
        {
            numericValue = (T) object;
        }
        catch (ClassCastException exception)
        {
            Log.writeDataToLogFile(2,
                    "Parameter '" + logParameterName + "' has an invalid type");

            throw new RuntimeException(exception);
        }

        if (numericValue.doubleValue() < min.doubleValue() || numericValue.doubleValue() > max.doubleValue())
        {
            Log.writeDataToLogFile(2,
                    "An error was detected in the parameter '" + logParameterName + "': range [" + min + " .. " + max + "]");

            throw new RuntimeException();
        }

        return numericValue;
    }

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
     * @param PATH
     * @param FILE_NAME
     * @param jsonElementTFunction
     * @param list
     * @param LIST_TYPE
     * @param <T>
     */
    public static <T> void readAndLogRules(final String PATH, final String FILE_NAME,
                                           Function<JsonElement, T> jsonElementTFunction, List<T> list, final String LIST_TYPE)
    {
        ParserGenericJsonScript.readRules(PATH, FILE_NAME, jsonElementTFunction, list, LIST_TYPE);

        if (!list.isEmpty())
        {
            Log.writeDataToLogFile(0,
                    String.format("Parsing '%s' list size = %d", list, list.size()));
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
