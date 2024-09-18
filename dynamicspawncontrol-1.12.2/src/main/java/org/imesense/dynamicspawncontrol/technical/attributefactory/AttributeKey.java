package org.imesense.dynamicspawncontrol.technical.attributefactory;

import javax.annotation.Nonnull;

/**
 *
 * @param <T>
 */
public final class AttributeKey<T>
{
    /**
     *
     */
    private final String name;

    /**
     *
     */
    private final AttributeType<T> type;

    /**
     *
     * @param type
     * @param name
     */
    public AttributeKey(@Nonnull AttributeType<T> type,
                        @Nonnull String name)
    {
        this.type = type;
        this.name = name;
    }

    /**
     *
     * @param type
     * @param code
     * @return
     * @param <T>
     */
    @Nonnull
    public static <T> AttributeKey<T> create(@Nonnull AttributeType<T> type,
                                             @Nonnull String code)
    {
        return new AttributeKey<>(type, code);
    }

    /**
     *
     * @return
     */
    @Nonnull
    public AttributeType<T> getType()
    {
        return this.type;
    }

    /**
     *
     * @return
     */
    @Nonnull
    public String getName()
    {
        return this.name;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString()
    {
        return "Key(" + this.name + ')';
    }
}
