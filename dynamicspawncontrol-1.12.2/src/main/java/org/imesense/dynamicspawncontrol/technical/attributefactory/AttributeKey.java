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
    private final String NAME;

    /**
     *
     */
    private final AttributeType<T> TYPE;

    /**
     *
     * @param type
     * @param name
     */
    public AttributeKey(@Nonnull AttributeType<T> type,
                        @Nonnull String name)
    {
        this.TYPE = type;
        this.NAME = name;
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
        return this.TYPE;
    }

    /**
     *
     * @return
     */
    @Nonnull
    public String getName()
    {
        return this.NAME;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString()
    {
        return "Key(" + this.NAME + ')';
    }
}
