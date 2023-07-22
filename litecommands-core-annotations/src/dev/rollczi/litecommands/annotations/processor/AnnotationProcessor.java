package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderExecutor;
import dev.rollczi.litecommands.meta.MetaHolder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface AnnotationProcessor<SENDER> {

    AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker);

    interface MetaHolderListener<A extends Annotation> {

        void call(Object instance, A annotation, MetaHolder metaHolder);

    }

    interface ClassListener<SENDER, A extends Annotation> {

        CommandBuilder<SENDER> call(Object instance, A annotation, CommandBuilder<SENDER> builder);

    }

    interface MethodListener<SENDER, A extends Annotation> {

        CommandBuilder<SENDER> call(Object instance, Method method, A annotation, CommandBuilder<SENDER> builder, CommandBuilderExecutor<SENDER> executorBuilder);

    }

    interface ParameterListener<SENDER, A extends Annotation> {

        CommandBuilder<SENDER> call(Object instance, Method method, Parameter parameter, A annotation, CommandBuilder<SENDER> builder, CommandBuilderExecutor<SENDER> executorBuilder);

    }


}
