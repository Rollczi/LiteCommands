package dev.rollczi.litecommands.shared;

import panda.std.Option;

import java.util.Map;

public final class MapUtil {

    private MapUtil() {}

    public static <E> Option<E> findByAssignableFromKey(Class<?> type, Map<Class<?>, E> map) {
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

}
