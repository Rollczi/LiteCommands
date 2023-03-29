package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.editor.CommandEditorContext;

import java.lang.annotation.Annotation;

public interface CommandAnnotationClassResolver<SENDER, A extends Annotation> {

    CommandEditorContext<SENDER> resolve(Object instance, A annotation, CommandEditorContext<SENDER> context);

}
