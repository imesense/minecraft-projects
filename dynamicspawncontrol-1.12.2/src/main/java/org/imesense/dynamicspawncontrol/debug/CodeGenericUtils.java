package org.imesense.dynamicspawncontrol.debug;

import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeKey;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
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
            Log.writeDataToLogFile(2, "Object<T> return null: " + message);
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
}
