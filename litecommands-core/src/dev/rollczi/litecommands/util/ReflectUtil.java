package dev.rollczi.litecommands.util;

import java.util.HashMap;
import java.util.Map;

public final class ReflectUtil {

    private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS = new HashMap<>();
    private static final Map<Class<?>, Class<?>> WRAPPERS_TO_PRIMITIVES = new HashMap<>();

    static {

        PRIMITIVES_TO_WRAPPERS.put(boolean.class, Boolean.class);
        PRIMITIVES_TO_WRAPPERS.put(byte.class, Byte.class);
        PRIMITIVES_TO_WRAPPERS.put(char.class, Character.class);
        PRIMITIVES_TO_WRAPPERS.put(double.class, Double.class);
        PRIMITIVES_TO_WRAPPERS.put(float.class, Float.class);
        PRIMITIVES_TO_WRAPPERS.put(int.class, Integer.class);
        PRIMITIVES_TO_WRAPPERS.put(long.class, Long.class);
        PRIMITIVES_TO_WRAPPERS.put(short.class, Short.class);
        PRIMITIVES_TO_WRAPPERS.put(void.class, Void.class);

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
