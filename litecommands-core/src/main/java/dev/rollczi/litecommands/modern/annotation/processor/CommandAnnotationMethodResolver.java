package dev.rollczi.litecommands.modern.annotation.processor;

import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface CommandAnnotationMethodResolver<SENDER, A extends Annotation> {

    CommandEditorContext<SENDER> resolve(Object instance, Method method, A annotation, CommandEditorContext<SENDER> context);

}
