package dev.rollczi.litecommands.modern.extension.annotated.executor;

import panda.std.Option;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

final class ParameterizedTypeUtils {

    static Option<Class<?>> extractType(Parameter parameter) {
        Type parameterizedType = parameter.getParameterizedType();

        if (parameterizedType instanceof ParameterizedType) {
            ParameterizedType parameterized = (ParameterizedType) parameterizedType;
            Type[] arguments = parameterized.getActualTypeArguments();

            if (arguments.length == 0) {
                return Option.none();
            }

            Type type = arguments[0];

            if (!(type instanceof Class)) {
                return Option.none();
            }

            return Option.of((Class<?>) type);
        }

        return Option.none();
    }

}
