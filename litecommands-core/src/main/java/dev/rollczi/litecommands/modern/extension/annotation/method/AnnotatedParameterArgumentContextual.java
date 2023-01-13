package dev.rollczi.litecommands.modern.extension.annotation.method;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContextual;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class AnnotatedParameterArgumentContextual<A extends Annotation, EXPECTED> extends ParameterContextual<EXPECTED> implements ArgumentContextual<A, EXPECTED> {

    private final AnnotatedParameterArgument<A> argument;

    private final A annotation;
    private final Class<A> annotationType;

    AnnotatedParameterArgumentContextual(AnnotatedParameterArgument<A> argument, Parameter parameter, Method method, A annotation, Class<A> annotationType, Class<EXPECTED> expectedType, Class<?> expectedWrapperType) {
        super(method, parameter, expectedType, expectedWrapperType);
        this.argument = argument;
        this.annotation = annotation;
        this.annotationType = annotationType;
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

}
