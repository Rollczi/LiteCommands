package dev.rollczi.litecommands.modern.annotation.processor;

import dev.rollczi.litecommands.modern.editor.CommandEditorContext;

import java.lang.annotation.Annotation;

public interface CommandAnnotationClassResolver<SENDER, A extends Annotation> {

    CommandEditorContext<SENDER> resolve(Object instance, A annotation, CommandEditorContext<SENDER> context);

}
