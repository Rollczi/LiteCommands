package dev.rollczi.litecommands.modern.command.method;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class AnnotatedParameterArgumentContext<A extends Annotation, EXPECTED> implements ArgumentContext<A, EXPECTED> {

    private final AnnotatedParameterArgument<A> argument;

    private final Parameter parameter;
    private final Method method;
    private final A annotation;

    private final Class<A> annotationType;
    private final Class<EXPECTED> expectedType;
    private final Class<?> expectedWrapperType;

    AnnotatedParameterArgumentContext(AnnotatedParameterArgument<A> argument, Parameter parameter, Method method, A annotation, Class<A> annotationType, Class<EXPECTED> expectedType, Class<?> expectedWrapperType) {
        this.argument = argument;
        this.parameter = parameter;
        this.method = method;
        this.annotation = annotation;
        this.annotationType = annotationType;
        this.expectedType = expectedType;
        this.expectedWrapperType = expectedWrapperType;
    }

    @Override
    public AnnotatedParameterArgument<A> getArgument() {
        return argument;
    }

    @Override
    public A getDeterminant() {
        return annotation;
    }

    @Override
    public Class<A> getDeterminantType() {
        return annotationType;
    }

    @Override
    public Class<EXPECTED> getExpectedType() {
        return expectedType;
    }

    @Override
    public Class<?> getExpectedWrapperType() {
        return expectedWrapperType;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public Method getMethod() {
        return method;
    }


}
