package dev.rollczi.litecommands.modern.annotation.processor;

import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;

import java.lang.annotation.Annotation;

public interface CommandAnnotationResolver<A extends Annotation> {

    CommandEditorContext resolve(Object instance, A annotation, CommandEditorContext context);

}
