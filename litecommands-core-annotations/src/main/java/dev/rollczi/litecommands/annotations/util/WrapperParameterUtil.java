package dev.rollczi.litecommands.annotations.util;

import dev.rollczi.litecommands.util.ReflectFormatUtil;
import dev.rollczi.litecommands.wrapper.WrappedExpectedService;
import dev.rollczi.litecommands.wrapper.WrapperFormat;
import panda.std.Option;

import java.lang.reflect.Parameter;

public final class WrapperParameterUtil {

    private WrapperParameterUtil() {
    }

    public static WrapperFormat<?> wrapperFormat(WrappedExpectedService wrappedExpectedService, Parameter parameter) {
        Class<?> expectedType = parameter.getType();
        Class<?> expectedWrapperType = Void.class;

        if (wrappedExpectedService.isWrapper(expectedType)) {
            Option<Class<?>> option = ParameterizedTypeUtil.extractFirstType(parameter);

            if (option.isEmpty()) {
                throw new IllegalArgumentException("Cannot extract expected type from parameter " + ReflectFormatUtil.parameter(parameter));
            }

            expectedWrapperType = expectedType;
            expectedType = option.get();
        }

        return new WrapperFormat<>(expectedType, expectedWrapperType);
    }

}
