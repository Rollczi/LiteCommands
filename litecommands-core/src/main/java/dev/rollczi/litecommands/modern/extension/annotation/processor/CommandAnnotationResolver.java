package dev.rollczi.litecommands.modern.extension.annotation.processor;

import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;

import java.lang.annotation.Annotation;

public interface CommandAnnotationResolver<A extends Annotation> {

    CommandEditorContext process(A annotation, CommandEditorContext context);

}
