package dev.rollczi.litecommands.modern.extension.annotation.command.argument;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContextual;
import dev.rollczi.litecommands.modern.extension.annotation.command.contextual.ParameterContextual;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ParameterArgumentContextual<A extends Annotation, EXPECTED> extends ParameterContextual<EXPECTED> implements ArgumentContextual<A, EXPECTED> {

    private final A annotation;
    private final Class<A> annotationType;

    ParameterArgumentContextual(Parameter parameter, Method method, A annotation, Class<A> annotationType, Class<EXPECTED> expectedType, Class<?> expectedWrapperType) {
        super(method, parameter, expectedType, expectedWrapperType);
        this.annotation = annotation;
        this.annotationType = annotationType;
    }

    @Override
    public A getDeterminant() {
        return this.annotation;
    }

    @Override
    public Class<A> getDeterminantType() {
        return this.annotationType;
    }

}
