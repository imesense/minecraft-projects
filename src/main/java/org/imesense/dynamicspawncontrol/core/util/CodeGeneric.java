package org.imesense.dynamicspawncontrol.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Supplier;

public final class CodeGeneric
{

    private static final Map<Class<?>, Object> INSTANCES = new java.util.concurrent.ConcurrentHashMap<>();

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

                        INSTANCES.put(_class, instance);

                        LogManager.info("Singleton instance created for class: " + _class.getName());
                    }
                }
            }

            T instance = (T) instanceField.get(null);

            if (instance != null && !INSTANCES.containsKey(_class))
            {
                INSTANCES.put(_class, instance);
            }

            return instance;
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

                        INSTANCES.put(_class, instance);

                        LogManager.info("Singleton instance created for class: " + _class.getName());
                    }
                }
            }

            T instance = (T) instanceField.get(null);

            if (instance != null && !INSTANCES.containsKey(_class))
            {
                INSTANCES.put(_class, instance);
            }

            return instance;
        }
        catch (Exception exception)
        {
            LogManager.error("Failed to create Singleton instance for class: " + _class.getName() +
                    ". Error: " + exception.getMessage());

            throw new RuntimeException("Failed to create Singleton instance for class: " + _class.getName(), exception);
        }
    }

    public static void logAllInstances()
    {
        try
        {
            LogManager.info("[InstanceLogger] Starting instance logging...");
            LogManager.info("[InstanceLogger] ===========================================");

            if (INSTANCES.isEmpty())
            {
                LogManager.warn("[InstanceLogger] No instances found in storage");
                return;
            }

            LogManager.info("[InstanceLogger] Total instances: " + INSTANCES.size());

            int counter = 1;
            for (Map.Entry<Class<?>, Object> entry : INSTANCES.entrySet())
            {
                Class<?> clazz = entry.getKey();
                Object instance = entry.getValue();

                String instanceId = getInstanceId(instance);
                String className = clazz.getSimpleName();
                String fullClassName = clazz.getName();

                LogManager.info(String.format("[InstanceLogger] %d. %s.instance: %s",
                        counter, className, instanceId));

                if (instance != null)
                {
                    LogManager.debug("[InstanceLogger]     Class: " + fullClassName);
                    LogManager.debug("[InstanceLogger]     HashCode: " + System.identityHashCode(instance));
                }

                counter++;
            }

            LogManager.info("[InstanceLogger] ===========================================");
            LogManager.info("[InstanceLogger] Instance logging completed");
        }
        catch (Exception exception)
        {
            LogManager.error("[InstanceLogger] Failed to log instances: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private static String getInstanceId(Object instance)
    {
        if (instance == null)
        {
            return "null";
        }

        String toString = instance.toString();
        String defaultToString = instance.getClass().getName() + "@" +
                Integer.toHexString(System.identityHashCode(instance));

        if (!toString.equals(defaultToString))
        {
            return toString;
        }

        return String.valueOf(System.identityHashCode(instance));
    }
}