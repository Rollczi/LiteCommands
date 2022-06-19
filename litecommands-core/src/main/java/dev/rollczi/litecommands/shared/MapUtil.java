package dev.rollczi.litecommands.shared;

import panda.std.Option;

import java.util.Map;

public final class MapUtil {

    private MapUtil() {}

    public static <E> Option<E> findByKeyAssignableFrom(Class<?> type, Map<Class<?>, E> map) {
        E handler = map.get(type);

        if (handler != null) {
            return Option.of(handler);
        }

        for (Map.Entry<Class<?>, E> entry : map.entrySet()) {
            if (type.isAssignableFrom(entry.getKey())) {
                return Option.of(entry.getValue());
            }
        }

        return Option.none();
    }

}
