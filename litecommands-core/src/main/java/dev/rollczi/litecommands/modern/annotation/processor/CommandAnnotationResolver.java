package dev.rollczi.litecommands.modern.annotation.processor;

import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;

import java.lang.annotation.Annotation;

public interface CommandAnnotationResolver<SENDER, A extends Annotation> {

    CommandEditorContext<SENDER> resolve(Object instance, A annotation, CommandEditorContext<SENDER> context);

}
