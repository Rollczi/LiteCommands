package dev.rollczi.litecommands.reflect;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ReflectIndex {

    private static final Map<Class<?>, List<Class<?>>> INTERFACE_INDEX = new ConcurrentHashMap<>();

    public static List<Class<?>> getInterfaces(Class<?> type) {
        return INTERFACE_INDEX.computeIfAbsent(type, key -> ReflectUtil.getInterfaces(type));
    }

}
