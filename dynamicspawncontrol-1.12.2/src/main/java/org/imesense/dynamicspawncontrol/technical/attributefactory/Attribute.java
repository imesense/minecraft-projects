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
     * @param attributeKey
     * @param multiKey
     */
    public Attribute(AttributeKey<T> attributeKey, boolean multiKey)
    {
        this.KEY = attributeKey;
        this.MULTI_KEY = multiKey;
    }

    /**
     *
     * @param attributeKey
     * @return
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> Attribute<Object> create(AttributeKey<T> attributeKey)
    {
        return (Attribute<Object>) new Attribute<>(attributeKey, false);
    }

    /**
     *
     * @param attributeKey
     * @return
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> Attribute<Object> createMulti(AttributeKey<T> attributeKey)
    {
        return (Attribute<Object>) new Attribute<>(attributeKey, true);
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
