package dev.rollczi.litecommands.util;

import panda.std.Option;

import java.util.Map;

public final class MapUtil {

    private MapUtil() {}

    public static <E> Option<E> findByInstanceOf(Class<?> type, Map<Class<?>, E> map) {
        E element = map.get(type);

        if (element != null) {
            return Option.of(element);
        }

        for (Map.Entry<Class<?>, E> entry : map.entrySet()) {
            if (type.isAssignableFrom(entry.getKey())) {
                return Option.of(entry.getValue());
            }
        }

        return Option.none();
    }

    public static <E> Option<E> findBySuperTypeOf(Class<?> type, Map<Class<?>, E> map) {
        Option<E> option = findKeySuperTypeOf0(type, map);

        if (option.isPresent()) {
            return option;
        }

        for (Class<?> anInterface : type.getInterfaces()) {
            E element = map.get(anInterface);

            if (element != null) {
                return Option.of(element);
            }
        }

        return Option.none();
    }

    private static <E> Option<E> findKeySuperTypeOf0(Class<?> type, Map<Class<?>, E> map) {
        E element = map.get(type);

        if (element != null) {
            return Option.of(element);
        }

        Class<?> superclass = type.getSuperclass();

        if (superclass != null && superclass != Object.class) {
            Option<E> option = findBySuperTypeOf(superclass, map);

            if (option.isPresent()) {
                return option;
            }
        }

        return Option.none();
    }



}
