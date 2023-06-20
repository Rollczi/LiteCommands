package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.util.WrapperParameterUtil;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

class ArgParameterArgument<EXPECTED> extends ParameterArgument<Arg, EXPECTED> {

    protected ArgParameterArgument(Method method, Parameter parameter, int parameterIndex, Arg annotation, Class<Arg> annotationType, WrapFormat<EXPECTED, ?> wrapFormat) {
        super(method, parameter, parameterIndex, annotation, annotationType, wrapFormat);
    }

    @Override
    public String getName() {
        String value = this.getAnnotation().value();

        if (!value.isEmpty()) {
            return value;
        }

        return super.getName();
    }

    @SuppressWarnings("unchecked")
    static <EXPECTED> ArgParameterArgument<EXPECTED> createArg(WrapperRegistry wrapperRegistry, Parameter parameter, Arg annotation) {
        Method method = (Method) parameter.getDeclaringExecutable();
        int index = Arrays.asList(method.getParameters()).indexOf(parameter);
        Class<Arg> annotationType = (Class<Arg>) annotation.annotationType();
        WrapFormat<?, ?> wrapFormat = WrapperParameterUtil.wrapperFormat(wrapperRegistry, parameter);

        return new ArgParameterArgument<>(method, parameter, index, annotation, annotationType, (WrapFormat<EXPECTED, ?>) wrapFormat);
    }

}
