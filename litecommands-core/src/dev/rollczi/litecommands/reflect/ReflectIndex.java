package dev.rollczi.litecommands.reflect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ReflectIndex {

    private final static Map<Class<?>, List<Class<?>>> INTERFACE_INDEX = new HashMap<>();

    public static List<Class<?>> getInterfaces(Class<?> type) {
        List<Class<?>> interfaces = INTERFACE_INDEX.get(type);

        if (interfaces == null) {
            interfaces = Arrays.asList(type.getInterfaces());
            INTERFACE_INDEX.put(type, interfaces);
        }

        return interfaces;
    }

}
