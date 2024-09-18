package org.imesense.dynamicspawncontrol.technical.attributefactory;

import javax.annotation.Nonnull;
import java.util.List;

/**
 *
 * @param <T>
 */
public final class AttributeType<T>
{
    /**
     *
     */
    public static final AttributeType<Long> LONG = create(Long.class);

    /**
     *
     */
    public static final AttributeType<Float> FLOAT = create(Float.class);

    /**
     *
     */
    public static final AttributeType<String> JSON = create(String.class);

    /**
     *
     */
    public static final AttributeType<Double> DOUBLE = create(Double.class);

    /**
     *
     */
    public static final AttributeType<String> STRING = create(String.class);

    /**
     *
     */
    public static final AttributeType<Boolean> BOOLEAN = create(Boolean.class);

    /**
     *
     */
    public static final AttributeType<Integer> INTEGER = create(Integer.class);

    /**
     *
     */
    public static final AttributeType<?> MAP = create(AttributeMap.class);

    /**
     *
     */
    public static final AttributeType<Object> OBJECT = new AttributeType<>(Object.class);

    /**
     *
     */
    private final Class<T> type;

    /**
     *
     * @param type
     */
    private AttributeType(@Nonnull Class<T> type)
    {
        this.type = type;
    }

    /**
     *
     * @param type
     * @return
     * @param <T>
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T> AttributeType<T> create(@Nonnull Class<? super T> type)
    {
        return new AttributeType<>((Class<T>) type);
    }

    /**
     *
     * @return
     */
    @Nonnull
    public Class<T> getType()
    {
        return this.type;
    }

    /**
     *
     * @param list
     * @return
     */
    @Nonnull
    public List<T> convert(@Nonnull List<T> list)
    {
        return list;
    }

    /**
     *
     * @param javaObject
     * @return
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public T convert(Object javaObject)
    {
        return (T) javaObject;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString()
    {
        return "Type(" + getType().getSimpleName() + ')';
    }
}
