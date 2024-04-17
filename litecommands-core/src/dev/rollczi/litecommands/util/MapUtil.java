package dev.rollczi.litecommands.util;

import dev.rollczi.litecommands.reflect.ReflectUtil;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Optional;

public final class MapUtil {

    private MapUtil() {}

    public static <E> Optional<E> findByInstanceOf(Class<?> type, Map<Class<?>, E> map) {
        E element = map.get(type);

        if (element != null) {
            return Optional.of(element);
        }

        for (Map.Entry<Class<?>, E> entry : map.entrySet()) {
            if (type.isAssignableFrom(entry.getKey())) {
                return Optional.ofNullable(entry.getValue());
            }
        }

        return Optional.empty();
    }

    public static <E> Optional<E> findBySuperTypeOf(Class<?> type, Map<Class<?>, E> map) {
        if (type.isArray()) {
            Optional<E> optional = findBySuperTypeOfArray(type.getComponentType(), map);

            if (optional.isPresent()) {
                return optional;
            }
        }

        Optional<E> option = findKeySuperTypeOf0(type, map);

        if (option.isPresent()) {
            return option;
        }

        for (Class<?> anInterface : ReflectUtil.getInterfaces(type)) {
            E element = map.get(anInterface);

            if (element != null) {
                return Optional.of(element);
            }
        }

        return findKeySuperTypeOf0(Object.class, map);
    }

    private static <E> Optional<E> findKeySuperTypeOf0(Class<?> type, Map<Class<?>, E> map) {
        E element = map.get(type);

        if (element != null) {
            return Optional.of(element);
        }

        Class<?> superclass = type.getSuperclass();

        if (superclass != null && superclass != Object.class) {
            Optional<E> option = findBySuperTypeOf(superclass, map);

            if (option.isPresent()) {
                return option;
            }
        }

        return Optional.empty();
    }

    private static <E> Optional<E> findBySuperTypeOfArray(Class<?> componentType, Map<Class<?>, E> map) {
        Optional<E> optional = findBySuperTypeOfArray0(componentType, map);

        if (optional.isPresent()) {
            return optional;
        }

        for (Class<?> anInterface : ReflectUtil.getInterfaces(componentType)) {
            Class<?> arrayType = ReflectUtil.getArrayType(anInterface);
            E element = map.get(arrayType);

            if (element != null) {
                return Optional.of(element);
            }
        }

        return findBySuperTypeOfArray0(Object.class, map);
    }

    private static <E> Optional<E> findBySuperTypeOfArray0(Class<?> componentType, Map<Class<?>, E> map) {
        Class<?> arrayType = ReflectUtil.getArrayType(componentType);
        E element = map.get(arrayType);

        if (element != null) {
            return Optional.of(element);
        }

        Class<?> superclass = componentType.getSuperclass();

        if (superclass != null && superclass != Object.class) {
            Optional<E> option = findBySuperTypeOfArray0(superclass, map);

            if (option.isPresent()) {
                return option;
            }
        }

        return Optional.empty();
    }

    public static Object[] getBoxedArrayFromPrimitiveArray(Object result) {
        if (result == null || !result.getClass().isArray()) {
            return new Object[0];
        }

        int length = Array.getLength(result);

        Object[] output = new Object[length];
        for (int i = 0; i < length; i++) {
            output[i] = Array.get(result, i);
        }

        return output;
    }

}
