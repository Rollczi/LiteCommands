package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderExecutor;
import dev.rollczi.litecommands.meta.MetaHolder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface CommandAnnotationMetaApplicator<SENDER, A extends Annotation> extends
    CommandAnnotationClassResolver<SENDER, A>,
    CommandAnnotationMethodResolver<SENDER, A> {

    void apply(Object instance, A annotation, MetaHolder metaHolder);

    @Override
    default CommandBuilder<SENDER> resolve(Object instance, A annotation, CommandBuilder<SENDER> context) {
        this.apply(instance, annotation, context);
        return context;
    }

    @Override
    default CommandBuilder<SENDER> resolve(Object instance, Method method, A annotation, CommandBuilder<SENDER> context, CommandBuilderExecutor<SENDER> executorBuilder) {
        this.apply(instance, annotation, executorBuilder);
        return context;
    }

}
