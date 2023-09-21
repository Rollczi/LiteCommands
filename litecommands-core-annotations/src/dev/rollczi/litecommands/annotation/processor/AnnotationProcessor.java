package dev.rollczi.litecommands.annotation.processor;

import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.command.CommandExecutorProvider;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.executor.CommandExecutorBuilder;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.requirement.Requirement;

import java.lang.annotation.Annotation;
import java.util.Optional;

public interface AnnotationProcessor<SENDER> {

    AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker);

    interface Listener<A extends Annotation> {
        void call(A annotation, MetaHolder metaHolder);
    }

    interface StructureListener<SENDER, A extends Annotation> {
        CommandBuilder<SENDER> call(A annotation, CommandBuilder<SENDER> builder);
    }

    interface StructureExecutorListener<SENDER, A extends Annotation> {
        CommandBuilder<SENDER> call(A annotation, CommandBuilder<SENDER> builder, CommandExecutorProvider<SENDER> executorProvider);
    }

    interface RequirementListener<SENDER, A extends Annotation> {
        Optional<Requirement<?>> call(AnnotationHolder<A, ?, ?> annotationHolder, CommandBuilder<SENDER> builder);
    }

    interface RequirementMetaListener<SENDER, A extends Annotation> {
        void call(AnnotationHolder<A, ?, ?> annotationHolder, CommandBuilder<SENDER> builder, Requirement<?> requirement);
    }

}
