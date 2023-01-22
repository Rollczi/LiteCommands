package dev.rollczi.litecommands.modern.extension.annotation.analyzer;

import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;
import dev.rollczi.litecommands.modern.extension.annotation.editor.AnnotationCommandEditorRegistry;

import java.lang.annotation.Annotation;

public class CommandAnnotationProcessor {

    private final CommandAnnotationRegistry commandAnnotationRegistry;
    private final AnnotationCommandEditorRegistry annotationCommandEditorRegistry;

    public CommandAnnotationProcessor(CommandAnnotationRegistry commandAnnotationRegistry, AnnotationCommandEditorRegistry annotationCommandEditorRegistry) {
        this.commandAnnotationRegistry = commandAnnotationRegistry;
        this.annotationCommandEditorRegistry = annotationCommandEditorRegistry;
    }

    public CommandRoute createRoute(Object instance) {
        Class<?> type = instance.getClass();
        CommandEditorContext context = CommandEditorContext.empty();

        for (Annotation annotation : type.getAnnotations()) {
            context = commandAnnotationRegistry.resolve(annotation, context);
        }

        annotationCommandEditorRegistry.edit()
    }

}
