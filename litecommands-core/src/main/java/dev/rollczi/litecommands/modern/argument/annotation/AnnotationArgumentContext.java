package dev.rollczi.litecommands.modern.argument.annotation;

import dev.rollczi.litecommands.modern.argument.ArgumentContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class AnnotationArgumentContext<A extends Annotation, EXPECTED> implements ArgumentContext<A, EXPECTED> {

    private final AnnotationArgument<A> argument;

    private final Parameter parameter;
    private final Method method;
    private final A annotation;

    private final Class<A> annotationType;
    private final Class<EXPECTED> expectedType;

    private AnnotationArgumentContext(AnnotationArgument<A> argument, Parameter parameter, Method method, A annotation, Class<A> annotationType, Class<EXPECTED> expectedType) {
        this.argument = argument;
        this.parameter = parameter;
        this.method = method;
        this.annotation = annotation;
        this.annotationType = annotationType;
        this.expectedType = expectedType;
    }

    @Override
    public AnnotationArgument<A> getArgument() {
        return argument;
    }

    @Override
    public A getContext() {
        return annotation;
    }

    @Override
    public Class<A> getContextType() {
        return annotationType;
    }

    @Override
    public Class<EXPECTED> getReturnType() {
        return expectedType;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public Method getMethod() {
        return method;
    }

    @SuppressWarnings("unchecked")
    public static <A extends Annotation, EXPECTED> AnnotationArgumentContext<A, EXPECTED> of(Parameter parameter, A annotation) {
        AnnotationArgument<A> argument = new AnnotationArgument<>();

        Method method = (Method) parameter.getDeclaringExecutable();
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        Class<EXPECTED> expectedType = (Class<EXPECTED>) method.getReturnType();

        return new AnnotationArgumentContext<>(argument, parameter, method, annotation, annotationType, expectedType);
    }

}
