package org.imesense.dynamicspawncontrol.debug;

import com.google.gson.JsonElement;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeKey;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.parsers.ParserJsonScripts;

import java.util.List;
import java.util.function.Function;

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
            Log.writeDataToLogFile(2, "Object return null: " + message);
            throw new NullPointerException(message);
        }

        return object;
    }

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
        catch (ClassCastException e)
        {
            Log.writeDataToLogFile(2, "Parameter '" + logParameterName + "' has an invalid type");
            throw new RuntimeException();
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
     * @param parameterTypes
     * @return
     * @param <T>
     */
    public static <T> boolean hasConstructorWithParameter(Class<T> clazz, Class<?>... parameterTypes)
    {
        try
        {
            clazz.getConstructor(parameterTypes);
            return true;
        }
        catch (NoSuchMethodException e)
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
        ParserJsonScripts.readRules(path, fileName, parser, list, listType);

        if (!list.isEmpty())
        {
            Log.writeDataToLogFile(0,
                    String.format("Parsing '%s' list size = %d", list, list.size()));
        }
    }
}
