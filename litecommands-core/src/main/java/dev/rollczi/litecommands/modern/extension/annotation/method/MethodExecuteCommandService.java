package dev.rollczi.litecommands.modern.extension.annotation.method;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpped.WrappedArgumentService;
import dev.rollczi.litecommands.shared.ReflectFormat;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class MethodExecuteCommandService {

    private final WrappedArgumentService wrappedArgumentService;

    public MethodExecuteCommandService(WrappedArgumentService wrappedArgumentService) {
        this.wrappedArgumentService = wrappedArgumentService;
    }

    public MethodCommandExecutor create(Method method) {
        List<ArgumentContext<Annotation, Object>> contexts = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            for (Annotation annotation : parameter.getAnnotations()) {
                contexts.add(this.argumentContext(parameter, annotation));
            }
        }

        return new MethodCommandExecutor(contexts, method); //TODO napisać prosty injector
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation, EXPECTED> AnnotatedParameterArgumentContext<A, EXPECTED> argumentContext(Parameter parameter, A annotation) {
        AnnotatedParameterArgument<A> argument = new AnnotatedParameterArgument<>();

        Method method = (Method) parameter.getDeclaringExecutable();
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        Class<?> expectedType = parameter.getType();
        Class<?> expectedWrapperType = expectedType;

        if (wrappedArgumentService.isWrapper(expectedType)) {
            Option<Class<?>> option = ParameterizedTypeUtils.extractType(parameter);

            if (option.isEmpty()) {
                throw new IllegalArgumentException("Cannot extract expected type from parameter " + ReflectFormat.parameter(parameter));
            }

            expectedType = option.get();
        }

        return new AnnotatedParameterArgumentContext<>(argument, parameter, method, annotation, annotationType, (Class<EXPECTED>) expectedType, expectedWrapperType);
    }

}
