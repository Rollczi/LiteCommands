package dev.rollczi.litecommands.folia.argument;

import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;
import dev.rollczi.litecommands.reflect.ReflectUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OldEnumAccessor {

    private static final Map<Class<?>, Method> VALUE_OF_METHODS = new HashMap<>();
    private static final Map<Class<?>, Method> NAME_METHODS = new HashMap<>();
    private static final Map<Class<?>, Method> VALUES_METHODS = new HashMap<>();

    public static boolean isAvailable() {
        return getType().isPresent();
    }

    public static Class<?> getTypeOrThrow() {
        return getType().orElseThrow(() -> new IllegalStateException("OldEnum is not available"));
    }

    public static Optional<Class<?>> getType() {
        try {
            return Optional.of(Class.forName("org.bukkit.util.OldEnum"));
        } catch (ClassNotFoundException classNotFoundException) {
            return Optional.empty();
        }
    }

    public static Object invokeValueOf(Class<?> type, String source) {
        if (!isInstanceOfOldEnum(type)) {
            throw new IllegalArgumentException("Type is not an instance of OldEnum");
        }

        Method valueOfMethod = VALUE_OF_METHODS.computeIfAbsent(type, key -> ReflectUtil.getMethod(type, "valueOf", String.class));
        try {
            return ReflectUtil.invokeStaticMethod(valueOfMethod, source);
        } catch (LiteCommandsReflectException exception) {
            throw exception.toRuntimeException();
        }
    }

    public static String invokeName(Object source) {
        Class<?> type = source.getClass();
        if (!isInstanceOfOldEnum(type)) {
            throw new IllegalArgumentException("Type is not an instance of OldEnum");
        }

        Method nameMethod = NAME_METHODS.computeIfAbsent(type, key -> ReflectUtil.getMethod(type, "name"));
        try {
            return ReflectUtil.invokeMethod(nameMethod, source);
        } catch (LiteCommandsReflectException exception) {
            throw exception.toRuntimeException();
        }
    }

    public static Object[] invokeValues(Class<?> type) {
        if (!isInstanceOfOldEnum(type)) {
            throw new IllegalArgumentException("Type is not an instance of OldEnum");
        }

        Method valuesMethod = VALUES_METHODS.computeIfAbsent(type, key -> ReflectUtil.getMethod(type, "values"));
        try {
            return ReflectUtil.invokeStaticMethod(valuesMethod);
        } catch (LiteCommandsReflectException exception) {
            throw exception.toRuntimeException();
        }
    }

    private static @NotNull Boolean isInstanceOfOldEnum(Class<?> type) {
        return getType()
            .map(oldEnum -> oldEnum.isAssignableFrom(type))
            .orElse(false);
    }

}
