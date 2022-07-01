package dev.rollczi.litecommands.argument;

import java.lang.reflect.Parameter;

public interface ParameterHandler {

    default boolean canHandle(Class<?> type, Parameter parameter) {
        return parameter.getType().equals(type);
    }

    default boolean canHandleAssignableFrom(Class<?> type, Parameter parameter) {
        return parameter.getType().isAssignableFrom(type);
    }

}
