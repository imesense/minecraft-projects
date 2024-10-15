package org.imesense.dynamicspawncontrol.debug;

import com.google.gson.JsonElement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeKey;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.parsers.ParserGenericJsonScripts;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

/**
 *
 */
public final class CodeGenericUtils
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
            Log.writeDataToLogFile(2, "Object or class return null: " + message);
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
        ParserGenericJsonScripts.readRules(path, fileName, parser, list, listType);

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

    /**
     *
     * @param provider
     * @param capability
     * @param facing
     * @return
     * @param <T>
     */
    @Nullable
    public static <T> T fetchCapability(@Nullable ICapabilityProvider provider, Capability<T> capability, @Nullable EnumFacing facing)
    {
        return provider != null && provider.hasCapability(capability, facing) ? provider.getCapability(capability, facing) : null;
    }

    /**
     *
     * @param worldIn
     * @param pos
     * @param capability
     * @param facing
     * @return
     * @param <T>
     */
    @Nullable
    public static <T> T fetchCapability(World worldIn, BlockPos pos, Capability<T> capability, @Nullable EnumFacing facing)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return fetchCapability(tileentity, capability, facing);
    }

    public static <T> T getValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
