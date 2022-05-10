package dev.rollczi.litecommands.argument;

import java.lang.reflect.Parameter;

public interface ParameterHandler {

    default boolean canHandle(Class<?> type, Parameter parameter) {
        return type.equals(parameter.getType());
    }

    default boolean canHandleAssignableFrom(Class<?> type, Parameter parameter) {
        return type.isAssignableFrom(parameter.getType());
    }

}
