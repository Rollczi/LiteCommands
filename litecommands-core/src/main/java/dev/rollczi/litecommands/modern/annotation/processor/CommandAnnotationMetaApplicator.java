package dev.rollczi.litecommands.modern.annotation.processor;

import dev.rollczi.litecommands.modern.editor.CommandEditorContext;
import dev.rollczi.litecommands.modern.editor.CommandEditorExecutorBuilder;
import dev.rollczi.litecommands.modern.meta.CommandMetaHolder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface CommandAnnotationMetaApplicator<SENDER, A extends Annotation> extends
    CommandAnnotationClassResolver<SENDER, A>,
    CommandAnnotationMethodResolver<SENDER, A> {

    void apply(Object instance, A annotation, CommandMetaHolder metaHolder);

    @Override
    default CommandEditorContext<SENDER> resolve(Object instance, A annotation, CommandEditorContext<SENDER> context) {
        apply(instance, annotation, context);
        return context;
    }

    @Override
    default CommandEditorContext<SENDER> resolve(Object instance, Method method, A annotation, CommandEditorContext<SENDER> context, CommandEditorExecutorBuilder<SENDER> executorBuilder) {
        apply(instance, annotation, executorBuilder);
        return context;
    }

}