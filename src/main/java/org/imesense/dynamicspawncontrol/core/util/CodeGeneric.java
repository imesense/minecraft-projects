package org.imesense.dynamicspawncontrol.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public final class CodeGeneric
{
    public CodeGeneric()
    {

    }

    public static boolean hasDefaultConstructor(Class<?> _class)
    {
        try
        {
            _class.getConstructor();

            return true;
        }
        catch (NoSuchMethodException exception)
        {
            return false;
        }
    }

    public static void logInitialization(final Class<?> _CLASS)
    {
        LogManager.info(String.format("Initializing a class: {%s}", _CLASS.getName()));
    }

    public static <T> T as(Object object, Class<T> _class)
    {
        return _class.isInstance(object) ?
                _class.cast(object) : null;
    }

    public static EnumCreatureType getCreatureType(Class<? extends Entity> _class)
    {
        try
        {
            EntityLiving entity =
                    (EntityLiving) _class.getConstructor(World.class).newInstance((World) null);

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
            LogManager.error("Failed to determine creature type for entity: " + _class.getName() + ", error: " + exception.getMessage());
        }

        return EnumCreatureType.CREATURE;
    }

    public static <T> T getInstance(Class<T> _class)
    {
        try
        {
            Field instanceField = _class.getDeclaredField("_INSTANCE");
            instanceField.setAccessible(true);

            if (instanceField.get(null) == null)
            {
                synchronized (_class)
                {
                    if (instanceField.get(null) == null)
                    {
                        LogManager.info("Creating Singleton instance for class: " + _class.getName());

                        T instance = _class.getDeclaredConstructor().newInstance();
                        instanceField.set(null, instance);

                        LogManager.info("Singleton instance created for class: " + _class.getName());
                    }
                }
            }

            return (T) instanceField.get(null);
        }
        catch (Exception exception)
        {
            LogManager.error("Failed to create Singleton instance for class: " + _class.getName() +
                    ". Error: " + exception.getMessage());

            throw new RuntimeException("Failed to create Singleton instance for class: " + _class.getName(), exception);
        }
    }

    public static <T> T getInstance(Class<T> _class, Supplier<T> supplier)
    {
        try
        {
            Field instanceField = _class.getDeclaredField("_INSTANCE");
            instanceField.setAccessible(true);

            if (instanceField.get(null) == null)
            {
                synchronized (_class)
                {
                    if (instanceField.get(null) == null)
                    {
                        LogManager.info("Creating Singleton instance for class: " + _class.getName());

                        T instance = supplier.get();
                        instanceField.set(null, instance);

                        LogManager.info("Singleton instance created for class: " + _class.getName());
                    }
                }
            }

            return (T) instanceField.get(null);
        }
        catch (Exception exception)
        {
            LogManager.error("Failed to create Singleton instance for class: " + _class.getName() +
                    ". Error: " + exception.getMessage());

            throw new RuntimeException("Failed to create Singleton instance for class: " + _class.getName(), exception);
        }
    }
}
