package dev.rollczi.litecommands.modern.command.argument.method;

import dev.rollczi.litecommands.modern.command.argument.ArgumentContext;
import dev.rollczi.litecommands.modern.command.argument.invocation.warpper.ExpectedValueService;
import dev.rollczi.litecommands.shared.ReflectFormat;
import panda.std.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class MethodExecuteCommandService {

    private final ExpectedValueService expectedValueService;

    public MethodExecuteCommandService(ExpectedValueService expectedValueService) {
        this.expectedValueService = expectedValueService;
    }

    public MethodCommandExecute create(Method method) {
        List<ArgumentContext<Annotation, Object>> contexts = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            for (Annotation annotation : parameter.getAnnotations()) {
                contexts.add(this.argumentContext(parameter, annotation));
            }
        }

        return new MethodCommandExecute(contexts);
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation, EXPECTED> AnnotatedParameterArgumentContext<A, EXPECTED> argumentContext(Parameter parameter, A annotation) {
        AnnotatedParameterArgument<A> argument = new AnnotatedParameterArgument<>();

        Method method = (Method) parameter.getDeclaringExecutable();
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        Class<?> expectedType = parameter.getType();
        Class<?> expectedWrapperType = expectedType;

        if (expectedValueService.isWrapper(expectedType)) {
            Option<Class<?>> option = ParameterizedTypeUtils.extractType(parameter);

            if (option.isEmpty()) {
                throw new IllegalArgumentException("Cannot extract expected type from parameter " + ReflectFormat.parameter(parameter));
            }

            expectedType = option.get();
        }

        return new AnnotatedParameterArgumentContext<>(argument, parameter, method, annotation, annotationType, (Class<EXPECTED>) expectedType, expectedWrapperType);
    }

}
