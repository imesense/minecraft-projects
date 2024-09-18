package org.imesense.dynamicspawncontrol.technical.attributefactory;

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
    private final HashMap<AttributeKey<?>, Object> values = new HashMap<>();

    /**
     *
     * @param key
     * @return
     */
    public boolean has(@Nonnull AttributeKey<?> key)
    {
        return this.values.containsKey(key);
    }

    /**
     *
     * @param key
     * @param value
     */
    public void set(@Nonnull AttributeKey<T> key, T value)
    {
        values.put(key, value);
    }

    /**
     *
     * @param key
     * @param value
     */
    public void setNonnull(@Nonnull AttributeKey<T> key, T value)
    {
        if (value != null)
        {
            this.values.put(key, value);
        }
    }

    /**
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public T get(@Nonnull AttributeKey<?> key)
    {
        return (T) this.values.get(key);
    }

    /**
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public Optional<T> getOptional(@Nonnull AttributeKey<T> key)
    {
        return Optional.ofNullable((T) this.values.get(key));
    }

    /**
     *
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
    public void addList(@Nonnull AttributeKey<?> key, T value)
    {
        if (!this.values.containsKey(key))
        {
            this.values.put(key, new ArrayList<>());
        }

        List<T> list = (List<T>) this.values.get(key);
        list.add(value);
    }

    /**
     *
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
    public void addListNonnull(@Nonnull AttributeKey<T> key, T value)
    {
        if (value == null)
        {
            return;
        }

        if (!this.values.containsKey(key))
        {
            this.values.put(key, new ArrayList<>());
        }

        List<T> list = (List<T>) this.values.get(key);
        list.add(value);
    }

    /**
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getList(@Nonnull AttributeKey<?> key)
    {
        if (!this.values.containsKey(key))
        {
            return Collections.emptyList();
        }

        return (List<String>) this.values.get(key);
    }

    /**
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Integer> getListI(@Nonnull AttributeKey<?> key)
    {
        if (!this.values.containsKey(key))
        {
            return Collections.emptyList();
        }

        return (List<Integer>) this.values.get(key);
    }

    /**
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AttributeMap<String>> getListA(@Nonnull AttributeKey<?> key)
    {
        if (!this.values.containsKey(key))
        {
            return Collections.emptyList();
        }

        return (List<AttributeMap<String>>) this.values.get(key);
    }
}
