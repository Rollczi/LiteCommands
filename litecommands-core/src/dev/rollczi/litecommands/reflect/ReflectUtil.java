package dev.rollczi.litecommands.reflect;

import java.util.HashMap;
import java.util.Map;

public final class ReflectUtil {

    private static final Map<Class<?>, Class<?>> WRAPPERS_TO_PRIMITIVES = new HashMap<>();

    static {
        WRAPPERS_TO_PRIMITIVES.put(Boolean.class, boolean.class);
        WRAPPERS_TO_PRIMITIVES.put(Byte.class, byte.class);
        WRAPPERS_TO_PRIMITIVES.put(Character.class, char.class);
        WRAPPERS_TO_PRIMITIVES.put(Double.class, double.class);
        WRAPPERS_TO_PRIMITIVES.put(Float.class, float.class);
        WRAPPERS_TO_PRIMITIVES.put(Integer.class, int.class);
        WRAPPERS_TO_PRIMITIVES.put(Long.class, long.class);
        WRAPPERS_TO_PRIMITIVES.put(Short.class, short.class);
        WRAPPERS_TO_PRIMITIVES.put(Void.class, void.class);
    }

    private ReflectUtil() {}

    public static boolean instanceOf(Object obj, Class<?> instanceOf) {
        return instanceOf(obj.getClass(), instanceOf);
    }

    public static boolean instanceOf(Class<?> clazz, Class<?> instanceOf) {
        if (instanceOf.isAssignableFrom(clazz)) {
            return true;
        }

        if (clazz.isPrimitive()) {
            return WRAPPERS_TO_PRIMITIVES.get(instanceOf) == clazz;
        }

        if (instanceOf.isPrimitive()) {
            return WRAPPERS_TO_PRIMITIVES.get(clazz) == instanceOf;
        }

        return false;
    }

}
