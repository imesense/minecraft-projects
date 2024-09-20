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
    private final boolean MULTI_KEY;

    /**
     *
     */
    private final AttributeKey<T> KEY;

    /**
     *
     * @param key
     * @param multiKey
     */
    public Attribute(AttributeKey<T> key, boolean multiKey)
    {
        this.KEY = key;
        this.MULTI_KEY = multiKey;
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
        return this.KEY;
    }

    /**
     *
     * @return
     */
    public boolean isMulti()
    {
        return this.MULTI_KEY;
    }
}
