package dev.rollczi.litecommands.annotations.util;

import dev.rollczi.litecommands.util.ReflectFormatUtil;
import dev.rollczi.litecommands.wrapper.WrappedExpectedService;
import dev.rollczi.litecommands.wrapper.WrapperFormat;
import panda.std.Option;

import java.lang.reflect.Parameter;

public final class WrapperParameterUtil {

    private WrapperParameterUtil() {
    }

    public static WrapperFormat<?, ?> wrapperFormat(WrappedExpectedService wrappedExpectedService, Parameter parameter) {
        Class<?> outParameterType = parameter.getType();

        if (wrappedExpectedService.isWrapper(outParameterType)) {
            Option<Class<?>> optionGenericType = ParameterizedTypeUtil.extractFirstType(parameter);

            if (optionGenericType.isEmpty()) {
                throw new IllegalArgumentException("Cannot extract expected type from parameter " + ReflectFormatUtil.parameter(parameter));
            }

            return WrapperFormat.of(optionGenericType.get(), outParameterType);
        }

        return WrapperFormat.notWrapped(outParameterType);
    }

}
