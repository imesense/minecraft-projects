package org.imesense.dynamicspawncontrol.core.cast;

public final class CastUtil
{
    private CastUtil() { }

    public static <T> To<T> to(Class<T> targetClass)
    {
        return new To<>(targetClass);
    }

    public static From from(Object obj)
    {
        return new From(obj);
    }

    public static final class To<T>
    {
        private final Class<T> targetClass;

        private To(Class<T> targetClass)
        {
            this.targetClass = targetClass;
        }

        public T from(Object obj)
        {
            return staticCast(obj, targetClass);
        }

        public T fromSafe(Object obj)
        {
            return staticCastSafe(obj, targetClass);
        }

        @SuppressWarnings("unchecked")
        public T reinterpret(Object obj)
        {
            return (T) obj;
        }
    }

    public static final class From
    {
        private final Object obj;

        private From(Object obj)
        {
            this.obj = obj;
        }

        public <T> T to(Class<T> targetClass)
        {
            return staticCast(obj, targetClass);
        }

        public <T> T toSafe(Class<T> targetClass)
        {
            return staticCastSafe(obj, targetClass);
        }

        @SuppressWarnings("unchecked")
        public <T> T reinterpret(Class<T> targetClass)
        {
            return (T) obj;
        }

        public boolean canCastTo(Class<?> targetClass)
        {
            return obj != null && targetClass.isInstance(obj);
        }

        public Object get()
        {
            return obj;
        }

        public boolean isInstanceOf(Class<?> targetClass)
        {
            return obj != null && targetClass.isInstance(obj);
        }
    }

    public static <T> T staticCast(Object obj, Class<T> targetClass)
    {
        if (obj == null)
        {
            return null;
        }

        if (targetClass.isInstance(obj))
        {
            return targetClass.cast(obj);
        }

        if (targetClass == Integer.class && obj instanceof Number)
        {
            return targetClass.cast(((Number) obj).intValue());
        }
        if (targetClass == Long.class && obj instanceof Number)
        {
            return targetClass.cast(((Number) obj).longValue());
        }
        if (targetClass == Double.class && obj instanceof Number)
        {
            return targetClass.cast(((Number) obj).doubleValue());
        }
        if (targetClass == Float.class && obj instanceof Number)
        {
            return targetClass.cast(((Number) obj).floatValue());
        }
        if (targetClass == Boolean.class && obj instanceof Boolean)
        {
            return targetClass.cast(obj);
        }
        if (targetClass == String.class)
        {
            return targetClass.cast(obj.toString());
        }

        return null;
    }

    public static <T> T staticCastOrThrow(Object obj, Class<T> targetClass)
    {
        T result = staticCast(obj, targetClass);
        if (result == null && obj != null)
        {
            throw new ClassCastException(
                    String.format("Cannot cast %s to %s",
                            obj.getClass().getName(),
                            targetClass.getName())
            );
        }
        return result;
    }

    public static <T> T staticCastSafe(Object obj, Class<T> targetClass)
    {
        try
        {
            return staticCast(obj, targetClass);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static boolean canCast(Object obj, Class<?> targetClass)
    {
        return obj != null && targetClass.isInstance(obj);
    }

    public static class Primitive
    {
        public static int toInt(Object obj)
        {
            return CastUtil.to(Integer.class).from(obj) != null ?
                    (Integer) CastUtil.to(Integer.class).from(obj) : 0;
        }

        public static long toLong(Object obj)
        {
            return CastUtil.to(Long.class).from(obj) != null ?
                    (Long) CastUtil.to(Long.class).from(obj) : 0L;
        }

        public static double toDouble(Object obj)
        {
            return CastUtil.to(Double.class).from(obj) != null ?
                    (Double) CastUtil.to(Double.class).from(obj) : 0.0;
        }

        public static boolean toBoolean(Object obj)
        {
            if (obj == null) return false;
            if (obj instanceof Boolean) return (Boolean) obj;
            if (obj instanceof Number) return ((Number) obj).intValue() != 0;
            if (obj instanceof String)
            {
                String str = ((String) obj).toLowerCase();
                return str.equals("true") || str.equals("1") || str.equals("yes");
            }
            return false;
        }

        public static String toString(Object obj)
        {
            return obj != null ? obj.toString() : null;
        }
    }
}
