package org.imesense.dynamicspawncontrol.technical.attributefactory;

/**
 *
 * @param <T>
 */
public final class Attribute<T>
{
    /**
     *
     */
    private final boolean multiKey;

    /**
     *
     */
    private final AttributeKey<T> key;

    /**
     *
     * @param key
     * @param multiKey
     */
    public Attribute(AttributeKey<T> key, boolean multiKey)
    {
        this.key = key;
        this.multiKey = multiKey;
    }

    /**
     *
     * @param key
     * @return
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> Attribute<Object> create(AttributeKey<T> key)
    {
        return (Attribute<Object>) new Attribute<>(key, false);
    }

    /**
     *
     * @param key
     * @return
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> Attribute<Object> createMulti(AttributeKey<T> key)
    {
        return (Attribute<Object>) new Attribute<>(key, true);
    }

    /**
     *
     * @return
     */
    public AttributeKey<T> getKey()
    {
        return this.key;
    }

    /**
     *
     * @return
     */
    public boolean isMulti()
    {
        return this.multiKey;
    }
}
