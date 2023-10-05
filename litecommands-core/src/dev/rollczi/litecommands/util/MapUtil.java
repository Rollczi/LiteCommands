package dev.rollczi.litecommands.util;

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
                return Optional.of(entry.getValue());
            }
        }

        return Optional.empty();
    }

    public static <E> Optional<E> findBySuperTypeOf(Class<?> type, Map<Class<?>, E> map) {
        Optional<E> option = findKeySuperTypeOf0(type, map);

        if (option.isPresent()) {
            return option;
        }

        for (Class<?> anInterface : type.getInterfaces()) {
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



}
