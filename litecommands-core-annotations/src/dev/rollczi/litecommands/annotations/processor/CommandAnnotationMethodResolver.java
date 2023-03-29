package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.editor.CommandEditorContext;
import dev.rollczi.litecommands.editor.CommandEditorExecutorBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface CommandAnnotationMethodResolver<SENDER, A extends Annotation> {

    CommandEditorContext<SENDER> resolve(Object instance, Method method, A annotation, CommandEditorContext<SENDER> context, CommandEditorExecutorBuilder<SENDER> executorBuilder);

}
