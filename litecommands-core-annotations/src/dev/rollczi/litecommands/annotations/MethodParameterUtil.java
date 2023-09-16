package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.reflect.ReflectFormatUtil;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import panda.std.Option;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

final class MethodParameterUtil {

    private MethodParameterUtil() {
    }

    static WrapFormat<?, ?> wrapperFormat(WrapperRegistry wrapperRegistry, Parameter parameter) {
        Class<?> outParameterType = parameter.getType();

        if (wrapperRegistry.isWrapper(outParameterType)) {
            Option<Class<?>> optionGenericType = extractFirstType(parameter);

            if (optionGenericType.isEmpty()) {
                throw new IllegalArgumentException("Cannot extract expected type from parameter " + ReflectFormatUtil.parameter(parameter));
            }

            return WrapFormat.of(optionGenericType.get(), outParameterType);
        }

        return WrapFormat.notWrapped(outParameterType);
    }

    private static Option<Class<?>> extractFirstType(Parameter parameter) {
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
