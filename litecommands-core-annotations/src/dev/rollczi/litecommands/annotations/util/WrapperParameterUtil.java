package dev.rollczi.litecommands.annotations.util;

import dev.rollczi.litecommands.util.ReflectFormatUtil;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import panda.std.Option;

import java.lang.reflect.Parameter;

public final class WrapperParameterUtil {

    private WrapperParameterUtil() {
    }

    public static WrapFormat<?, ?> wrapperFormat(WrapperRegistry wrapperRegistry, Parameter parameter) {
        Class<?> outParameterType = parameter.getType();

        if (wrapperRegistry.isWrapper(outParameterType)) {
            Option<Class<?>> optionGenericType = ParameterizedTypeUtil.extractFirstType(parameter);

            if (optionGenericType.isEmpty()) {
                throw new IllegalArgumentException("Cannot extract expected type from parameter " + ReflectFormatUtil.parameter(parameter));
            }

            return WrapFormat.of(optionGenericType.get(), outParameterType);
        }

        return WrapFormat.notWrapped(outParameterType);
    }

}
