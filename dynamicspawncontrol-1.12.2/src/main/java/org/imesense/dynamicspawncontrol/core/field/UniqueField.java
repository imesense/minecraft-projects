package org.imesense.dynamicspawncontrol.core.field;

import net.minecraft.client.Minecraft;
import org.imesense.dynamicspawncontrol.core.annotation.UniqueObject;
import scala.util.Random;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public final class UniqueField
{
    /**
     *
     */
    private static volatile UniqueField instance;

    /**
     *
     */
    @UniqueObject
    public static final Random RANDOM = new Random();

    /**
     *
     */
    @UniqueObject
    public static final Minecraft CLIENT = Minecraft.getMinecraft();

    /**
     *
     */
    @UniqueObject
    public static final Boolean LOGGING_CONSOLE_LEVEL_DEBUG = "debug".equalsIgnoreCase(System.getProperty("forge.logging.console.level"));

    /**
     *
     */
    private UniqueField() throws IllegalAccessException
    {
        this.validateUniqueFields(this);
    }

    /**
     *
     */
    public static UniqueField getInstance() throws IllegalAccessException
    {
        if (instance == null)
        {
            synchronized (UniqueField.class)
            {
                if (instance == null)
                {
                    instance = new UniqueField();
                }
            }
        }

        return instance;
    }

    /**
     *
     */
    private void validateUniqueFields(Object object) throws IllegalAccessException
    {
        Set<Object> uniqueValues = new HashSet<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields)
        {
            if (field.isAnnotationPresent(UniqueObject.class))
            {
                field.setAccessible(true);

                Object object1 = field.get(object);

                if (!uniqueValues.add(object1))
                {
                    throw new IllegalStateException("Duplicate field value detected: " + field.getName());
                }
            }
        }
    }
}

