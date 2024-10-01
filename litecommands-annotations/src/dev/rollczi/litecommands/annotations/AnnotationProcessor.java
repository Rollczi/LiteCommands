package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.command.CommandExecutorProvider;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.requirement.Requirement;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

public interface AnnotationProcessor<SENDER> {

    AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker);

    interface AnyListener<A extends Annotation> {
        void call(A annotation, MetaHolder metaHolder);
    }

    interface ClassListener<SENDER, A extends Annotation> {
        CommandBuilder<SENDER> call(Class<?> classType, A annotation, CommandBuilder<SENDER> builder);
    }

    interface MethodListener<SENDER, A extends Annotation> {
        void call(Method method, A annotation, CommandBuilder<SENDER> builder, CommandExecutorProvider<SENDER> executorProvider);
    }

    interface ParameterListener<SENDER, A extends Annotation> {
        Optional<Requirement<?>> call(Parameter parameter, AnnotationHolder<A, ?> annotationHolder, CommandBuilder<SENDER> builder);
    }

    interface ParameterRequirementListener<SENDER, A extends Annotation> {
        void call(Parameter parameter, AnnotationHolder<A, ?> annotationHolder, CommandBuilder<SENDER> builder, Requirement<?> requirement);
    }

}
