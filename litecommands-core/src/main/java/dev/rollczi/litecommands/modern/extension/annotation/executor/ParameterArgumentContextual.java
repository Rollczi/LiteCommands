package dev.rollczi.litecommands.modern.extension.annotation.executor;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContextual;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ParameterArgumentContextual<A extends Annotation, EXPECTED> extends ParameterContextual<EXPECTED> implements ArgumentContextual<A, EXPECTED> {

    private final ParameterArgument<A> argument;

    private final A annotation;
    private final Class<A> annotationType;

    ParameterArgumentContextual(ParameterArgument<A> argument, Parameter parameter, Method method, A annotation, Class<A> annotationType, Class<EXPECTED> expectedType, Class<?> expectedWrapperType) {
        super(method, parameter, expectedType, expectedWrapperType);
        this.argument = argument;
        this.annotation = annotation;
        this.annotationType = annotationType;
    }

    @Override
    public ParameterArgument<A> getArgument() {
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

}
