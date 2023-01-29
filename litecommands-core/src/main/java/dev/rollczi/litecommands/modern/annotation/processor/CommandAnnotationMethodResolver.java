package dev.rollczi.litecommands.modern.annotation.processor;

import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface CommandAnnotationMethodResolver<A extends Annotation> {

    CommandEditorContext resolve(Object instance, Method method, A annotation, CommandEditorContext context);

}
