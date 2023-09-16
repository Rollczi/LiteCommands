package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.util.WrapperParameterUtil;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class ParameterArgument<A extends Annotation, EXPECTED> implements Argument<EXPECTED> {

    private final Method method;
    private final Parameter parameter;
    private final int parameterIndex;
    private final A annotation;
    private final Class<A> annotationType;

    private final WrapFormat<EXPECTED, ?> wrapFormat;

    private @Nullable String overrideName;

    @SuppressWarnings("unchecked")
    public ParameterArgument(WrapperRegistry wrapperRegistry, Parameter parameter, A annotation) {
        Method method = (Method) parameter.getDeclaringExecutable();
        int index = Arrays.asList(method.getParameters()).indexOf(parameter);
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        WrapFormat<?, ?> wrapFormat = WrapperParameterUtil.wrapperFormat(wrapperRegistry, parameter);

        this.method = method;
        this.parameter = parameter;
        this.parameterIndex = index;
        this.wrapFormat = (WrapFormat<EXPECTED, ?>) wrapFormat;
        this.annotation = annotation;
        this.annotationType = annotationType;
    }

    public Method getMethod() {
        return this.method;
    }

    public Parameter getParameter() {
        return this.parameter;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public A getAnnotation() {
        return annotation;
    }

    public Class<A> getAnnotationType() {
        return annotationType;
    }

    @Override
    public WrapFormat<EXPECTED, ?> getWrapperFormat() {
        return wrapFormat;
    }

    @Override
    public String getName() {
        if (overrideName != null) {
            return overrideName;
        }

        return this.parameter.getName();
    }

    void overrideName(String name) {
        this.overrideName = name;
    }

}
