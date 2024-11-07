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
    public static final Boolean IDEA_RT = System.getProperty("java.class.path").toLowerCase().contains("idea_rt.jar");

    /**
     *
     */
    public UniqueField() throws IllegalAccessException
    {
        this.validateUniqueFields(this);
    }

    /**
     *
     * @param object
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
