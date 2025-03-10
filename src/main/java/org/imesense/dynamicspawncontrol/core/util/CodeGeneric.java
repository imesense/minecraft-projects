package org.imesense.dynamicspawncontrol.core.util;

import com.google.gson.JsonElement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

/**
 *
 */
public final class CodeGeneric
{
    /**
     *
     * @param _clazz
     * @return
     */
    public static boolean hasDefaultConstructor(Class<?> _clazz)
    {
        try
        {
            _clazz.getConstructor();
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
     * @param _clazz
     * @return
     */
    public static EnumCreatureType getCreatureType(Class<? extends Entity> _clazz)
    {
        try
        {
            EntityLiving entity =
                    (EntityLiving) _clazz.getConstructor(World.class).newInstance((World) null);

            if (entity.isCreatureType(EnumCreatureType.MONSTER, false))
            {
                return EnumCreatureType.MONSTER;
            }
            else if (entity.isCreatureType(EnumCreatureType.CREATURE, false))
            {
                return EnumCreatureType.CREATURE;
            }
            else if (entity.isCreatureType(EnumCreatureType.AMBIENT, false))
            {
                return EnumCreatureType.AMBIENT;
            }
            else if (entity.isCreatureType(EnumCreatureType.WATER_CREATURE, false))
            {
                return EnumCreatureType.WATER_CREATURE;
            }
        }
        catch (Exception exception)
        {
            Log.writeDataToLogFile(0, "Failed to determine creature type for entity: " + _clazz.getName() + ", error: " + exception.getMessage());
        }


        return EnumCreatureType.CREATURE;
    }

    public static <T> T getInstance(Class<T> _clazz)
    {
        try
        {
            Field instanceField = _clazz.getDeclaredField("_INSTANCE");
            instanceField.setAccessible(true);

            if (instanceField.get(null) == null)
            {
                synchronized (_clazz)
                {
                    if (instanceField.get(null) == null)
                    {
                        T instance = _clazz.getDeclaredConstructor().newInstance();
                        instanceField.set(null, instance);
                    }
                }
            }

            return (T) instanceField.get(null);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to create Singleton instance for class: " + _clazz.getName(), e);
        }
    }
}
