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
     * @param attributeType
     * @param name
     */
    public AttributeKey(@Nonnull AttributeType<T> attributeType,
                        @Nonnull String name)
    {
        this.TYPE = attributeType;
        this.NAME = name;
    }

    /**
     *
     * @param attributeType
     * @param code
     * @return
     * @param <T>
     */
    @Nonnull
    public static <T> AttributeKey<T> create(@Nonnull AttributeType<T> attributeType,
                                             @Nonnull String code)
    {
        return new AttributeKey<>(attributeType, code);
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
