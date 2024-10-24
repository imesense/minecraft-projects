package org.imesense.dynamicspawncontrol;

import net.minecraft.client.Minecraft;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotation.UniqueObject;

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
    private final Minecraft CLIENT = Minecraft.getMinecraft();

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

                Object value = field.get(object);

                if (!uniqueValues.add(value))
                {
                    throw new IllegalStateException("Duplicate field value detected: " + field.getName());
                }
            }
        }
    }
}
