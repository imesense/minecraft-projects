package org.imesense.dynamicspawncontrol.debug;

import com.google.gson.JsonElement;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeKey;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.parser.ParserGenericJsonScript;

import java.util.List;
import java.util.function.Function;

/**
 *
 */
public final class CodeGenericUtil
{
    /**
     *
     * @param mobMap
     * @param parameterName
     * @param min
     * @param max
     * @param logParameterName
     * @return
     * @param <T>
     */
    public static <T extends Number> T checkParameter(AttributeMap<?> mobMap, AttributeKey<?> parameterName, T min, T max, String logParameterName)
    {
        Object value = mobMap.get(parameterName);

        if (value == null)
        {
            Log.writeDataToLogFile(2, "Parameter '" + logParameterName + "' missing or return null");
            throw new RuntimeException();
        }

        T numericValue;

        try
        {
            numericValue = (T) value;
        }
        catch (ClassCastException exception)
        {
            Log.writeDataToLogFile(2, "Parameter '" + logParameterName + "' has an invalid type");
            throw new RuntimeException(exception);
        }

        if (numericValue.doubleValue() < min.doubleValue() || numericValue.doubleValue() > max.doubleValue())
        {
            Log.writeDataToLogFile(2, "An error was detected in the parameter '" + logParameterName + "': range [" + min + " .. " + max + "]");
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
     * @param fileName
     * @param parser
     * @param list
     * @param listType
     * @param <T>
     */
    public static <T> void readAndLogRules(final String path, final String fileName, Function<JsonElement, T> parser, List<T> list, final String listType)
    {
        ParserGenericJsonScript.readRules(path, fileName, parser, list, listType);

        if (!list.isEmpty())
        {
            Log.writeDataToLogFile(0,
                    String.format("Parsing '%s' list size = %d", list, list.size()));
        }
    }

    /**
     *
     * @param getClass
     * @param <T>
     */
    public static <T> void printInitClassToLog(final Class<T> getClass)
    {
        Log.writeDataToLogFile(0, String.format("Initializing a class: {%s}", getClass.getName()));
    }

    /**
     *
     * @param getObject
     * @param getClass
     * @return
     * @param <T>
     */
    public static <T> T as(Object getObject, Class<T> getClass)
    {
        return getClass.isInstance(getObject) ? getClass.cast(getObject) : null;
    }
}
