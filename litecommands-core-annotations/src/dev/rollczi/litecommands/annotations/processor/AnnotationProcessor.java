package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderExecutor;
import dev.rollczi.litecommands.meta.MetaHolder;

import java.lang.annotation.Annotation;

public interface AnnotationProcessor<SENDER> {

    AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker);

    interface Listener<A extends Annotation> {

        void call(A annotation, MetaHolder metaHolder);

    }

    interface StructureListener<SENDER, A extends Annotation> {

        CommandBuilder<SENDER> call(A annotation, CommandBuilder<SENDER> builder);

    }

    interface StructureExecutorListener<SENDER, A extends Annotation> {

        CommandBuilder<SENDER> call(A annotation, CommandBuilder<SENDER> builder, CommandBuilderExecutor<SENDER> executorBuilder);

    }

    interface RequirementListener<SENDER, A extends Annotation> {

        CommandBuilder<SENDER> call(A annotation, CommandBuilder<SENDER> builder, CommandBuilderExecutor<SENDER> executorBuilder);

    }


}
