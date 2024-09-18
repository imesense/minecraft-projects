package org.imesense.dynamicspawncontrol.technical.attributefactory;

import javax.annotation.Nonnull;

public final class AttributeKey<T>
{
    private final String name;

    private final AttributeType<T> type;

    public AttributeKey(@Nonnull AttributeType<T> type,
                        @Nonnull String name)
    {
        this.type = type;
        this.name = name;
    }

    @Nonnull
    public static <T> AttributeKey<T> create(@Nonnull AttributeType<T> type,
                                             @Nonnull String code)
    {
        return new AttributeKey<>(type, code);
    }

    @Nonnull
    public AttributeType<T> getType()
    {
        return this.type;
    }

    @Nonnull
    public String getName()
    {
        return this.name;
    }

    @Override
    public String toString()
    {
        return "Key(" + this.name + ')';
    }
}
