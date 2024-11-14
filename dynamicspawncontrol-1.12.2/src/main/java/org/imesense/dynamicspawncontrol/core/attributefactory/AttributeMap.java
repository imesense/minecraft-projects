package org.imesense.dynamicspawncontrol.core.attributefactory;

import javax.annotation.Nonnull;
import java.util.*;

/**
 *
 * @param <T>
 */
public final class AttributeMap<T>
{
    /**
     *
     */
    private final HashMap<AttributeKey<?>, Object> VALUES = new HashMap<>();

    /**
     *
     * @param attributeKey
     * @return
     */
    public boolean has(@Nonnull AttributeKey<?> attributeKey)
    {
        return this.VALUES.containsKey(attributeKey);
    }

    /**
     *
     * @param attributeKey
     * @param value
     */
    public void set(@Nonnull AttributeKey<T> attributeKey, T value)
    {
        this.VALUES.put(attributeKey, value);
    }

    /**
     *
     * @param attributeKey
     * @param value
     */
    public void setNonnull(@Nonnull AttributeKey<T> attributeKey, T value)
    {
        if (value != null)
        {
            this.VALUES.put(attributeKey, value);
        }
    }

    /**
     *
     * @param attributeKey
     * @return
     */
    @SuppressWarnings("unchecked")
    public T get(@Nonnull AttributeKey<?> attributeKey)
    {
        return (T) this.VALUES.get(attributeKey);
    }

    /**
     *
     * @param attributeKey
     * @return
     */
    @SuppressWarnings("unchecked")
    public Optional<T> getOptional(@Nonnull AttributeKey<T> attributeKey)
    {
        return Optional.ofNullable((T) this.VALUES.get(attributeKey));
    }

    /**
     *
     * @param attributeKey
     * @param value
     */
    @SuppressWarnings("unchecked")
    public void addList(@Nonnull AttributeKey<?> attributeKey, T value)
    {
        if (!this.VALUES.containsKey(attributeKey))
        {
            this.VALUES.put(attributeKey, new ArrayList<>());
        }

        List<T> list = (List<T>) this.VALUES.get(attributeKey);
        list.add(value);
    }

    /**
     *
     * @param attributeKey
     * @param value
     */
    @SuppressWarnings("unchecked")
    public void addListNonnull(@Nonnull AttributeKey<T> attributeKey, T value)
    {
        if (value == null)
        {
            return;
        }

        if (!this.VALUES.containsKey(attributeKey))
        {
            this.VALUES.put(attributeKey, new ArrayList<>());
        }

        List<T> list = (List<T>) this.VALUES.get(attributeKey);
        list.add(value);
    }

    /**
     *
     * @param attributeKey
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getList(@Nonnull AttributeKey<?> attributeKey)
    {
        if (!this.VALUES.containsKey(attributeKey))
        {
            return Collections.emptyList();
        }

        return (List<String>) this.VALUES.get(attributeKey);
    }

    /**
     *
     * @param attributeKey
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Integer> getListI(@Nonnull AttributeKey<?> attributeKey)
    {
        if (!this.VALUES.containsKey(attributeKey))
        {
            return Collections.emptyList();
        }

        return (List<Integer>) this.VALUES.get(attributeKey);
    }

    /**
     *
     * @param attributeKey
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AttributeMap<String>> getListA(@Nonnull AttributeKey<?> attributeKey)
    {
        if (!this.VALUES.containsKey(attributeKey))
        {
            return Collections.emptyList();
        }

        return (List<AttributeMap<String>>) this.VALUES.get(attributeKey);
    }
}
