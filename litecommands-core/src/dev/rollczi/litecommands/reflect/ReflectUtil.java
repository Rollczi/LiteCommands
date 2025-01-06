package dev.rollczi.litecommands.reflect;

import dev.rollczi.litecommands.LiteCommandsException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ReflectUtil {

    private static final Map<Class<?>, Class<?>> WRAPPERS_TO_PRIMITIVES = new HashMap<>();
    private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_BOXED = new HashMap<>();

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

        PRIMITIVES_TO_BOXED.put(boolean.class, Boolean.class);
        PRIMITIVES_TO_BOXED.put(byte.class, Byte.class);
        PRIMITIVES_TO_BOXED.put(char.class, Character.class);
        PRIMITIVES_TO_BOXED.put(double.class, Double.class);
        PRIMITIVES_TO_BOXED.put(float.class, Float.class);
        PRIMITIVES_TO_BOXED.put(int.class, Integer.class);
        PRIMITIVES_TO_BOXED.put(long.class, Long.class);
        PRIMITIVES_TO_BOXED.put(short.class, Short.class);
        PRIMITIVES_TO_BOXED.put(void.class, Void.class);
    }

    private ReflectUtil() {}

    public static Class<?> getArrayType(Class<?> componentType) {
        return Array.newInstance(componentType, 0).getClass();
    }

    public static Class<?> boxedToPrimitive(Class<?> clazz) {
        return WRAPPERS_TO_PRIMITIVES.get(clazz);
    }

    public static Class<?> primitiveToBoxed(Class<?> clazz) {
        return PRIMITIVES_TO_BOXED.get(clazz);
    }

    public static boolean instanceOf(Object obj, Class<?> instanceOf) {
        if (obj == null) {
            return false;
        }

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

    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            Field declaredField = clazz.getDeclaredField(fieldName);

            declaredField.setAccessible(true);
            return declaredField;
        }
        catch (NoSuchFieldException exception) {
            if (clazz.getSuperclass() != null) {
                try {
                    return getField(clazz.getSuperclass(), fieldName);
                }
                catch (LiteCommandsException ignored) { }
            }

            throw new LiteCommandsReflectException("Unable to find field " + fieldName + " in " + clazz);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> getClass(String className) {
        try {
            return (Class<? extends T>) Class.forName(className, true, ReflectUtil.class.getClassLoader());
        }
        catch (ClassNotFoundException exception) {
            throw new LiteCommandsReflectException("Unable to find class " + className, exception);
        }
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        try {
            Method declaredMethod = clazz.getDeclaredMethod(methodName, params);

            declaredMethod.setAccessible(true);
            return declaredMethod;
        }
        catch (NoSuchMethodException exception) {
            if (clazz.getSuperclass() != null) {
                try {
                    return getMethod(clazz.getSuperclass(), methodName, params);
                }
                catch (LiteCommandsException ignored) {}
            }

            throw new LiteCommandsReflectException(String.format("Unable to find method %s(%s) in %s", methodName, Arrays.toString(params), clazz));
        }
    }

    public static List<Class<?>> getInterfaces(Class<?> type) {
        Class<?>[] current = type.getInterfaces();

        if (current.length == 0) {
            return Collections.emptyList();
        }

        List<Class<?>> interfaces = new ArrayList<>();

        for (Class<?> anInterface : type.getInterfaces()) {
            interfaces.add(anInterface);
            interfaces.addAll(getInterfaces(anInterface));
        }

        return interfaces;
    }

    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(Method method, Object instance, Object... params) {
        try {
            return (T) method.invoke(instance, params);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new LiteCommandsReflectException("Unable to invoke method " + method.getName() + " in " + instance.getClass(), invocationTargetException.getCause());
        }
        catch (IllegalAccessException exception) {
            throw new LiteCommandsReflectException("Cannot access method " + method.getName() + " in " + instance.getClass(), exception);
        }
    }

    public static <T> T invokeStaticMethod(Method method, Object... params) {
        return invokeMethod(method, null, params);
    }

    public static void setValue(Field field, Object instance, Object value) {
        try {
            field.set(instance, value);
        }
        catch (Exception exception) {
            throw new LiteCommandsReflectException("Unable to set field " + field.getName() + " in " + instance.getClass(), exception);
        }
    }

    public static void setStaticValue(Field field, Object value) {
        setValue(field, null, value);
    }

    public static Object getValue(Field field, Object instance) {
        try {
            return field.get(instance);
        }
        catch (Exception exception) {
            throw new LiteCommandsReflectException("Unable to get field " + field.getName() + " in " + instance.getClass(), exception);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFromField(Object instance, String fieldName) {
        return (T) getValue(getField(instance.getClass(), fieldName), instance);
    }

    public static <T> T getFromMethod(Object instance, String methodName, Object... params) {
        return invokeMethod(getMethod(instance.getClass(), methodName, getClasses(params)), instance, params);
    }

    private static Class<?>[] getClasses(Object[] params) {
        Class<?>[] classes = new Class<?>[params.length];

        for (int i = 0; i < params.length; i++) {
            classes[i] = params[i].getClass();
        }

        return classes;
    }


    public static List<Method> getMethods(Class<?> type) {
        List<Method> methods = new ArrayList<>();

        for (Method declaredMethod : type.getDeclaredMethods()) {
            methods.add(declaredMethod);
        }

        Class<?> superclass = type.getSuperclass();

        if (superclass != Object.class) {
            methods.addAll(getMethods(superclass));
        }

        return methods;
    }

}
