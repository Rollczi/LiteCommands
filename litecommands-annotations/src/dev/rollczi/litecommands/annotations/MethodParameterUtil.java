package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.prettyprint.PrettyPrintParameter;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

final class MethodParameterUtil {

    private MethodParameterUtil() {
    }

    static WrapFormat<?, ?> wrapperFormat(WrapperRegistry wrapperRegistry, Parameter parameter) {
        Class<?> outParameterType = parameter.getType();

        if (wrapperRegistry.isWrapper(outParameterType)) {
            Optional<Type> optionGenericType = extractFirstType(parameter);

            if (!optionGenericType.isPresent()) {
                throw new IllegalArgumentException("Cannot extract expected type from parameter " + PrettyPrintParameter.formatParameter(parameter));
            }

            return WrapFormat.of(TypeToken.of(optionGenericType.get()), TypeToken.of(parameter.getParameterizedType()));
        }

        return WrapFormat.notWrapped(TypeToken.of(parameter.getParameterizedType()));
    }

    private static Optional<Type> extractFirstType(Parameter parameter) {
        Type parameterizedType = parameter.getParameterizedType();

        if (parameterizedType instanceof ParameterizedType) {
            ParameterizedType parameterized = (ParameterizedType) parameterizedType;
            Type[] arguments = parameterized.getActualTypeArguments();

            if (arguments.length == 0) {
                return Optional.empty();
            }

            return Optional.of(arguments[0]);
        }

        return Optional.empty();
    }

}
