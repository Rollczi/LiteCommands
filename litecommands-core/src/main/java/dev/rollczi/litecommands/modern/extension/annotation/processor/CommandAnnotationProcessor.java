package dev.rollczi.litecommands.modern.extension.annotation.processor;

import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;
import dev.rollczi.litecommands.modern.extension.annotation.editor.AnnotationCommandEditorService;

import java.lang.annotation.Annotation;

public class CommandAnnotationProcessor {

    private final CommandAnnotationRegistry commandAnnotationRegistry;
    private final AnnotationCommandEditorService annotationCommandEditorRegistry;

    public CommandAnnotationProcessor(CommandAnnotationRegistry commandAnnotationRegistry, AnnotationCommandEditorService annotationCommandEditorRegistry) {
        this.commandAnnotationRegistry = commandAnnotationRegistry;
        this.annotationCommandEditorRegistry = annotationCommandEditorRegistry;
    }

    public CommandEditorContext createContext(Object instance) {
        Class<?> type = instance.getClass();
        CommandEditorContext context = CommandEditorContext.empty();

        for (Annotation annotation : type.getAnnotations()) {
            context = this.commandAnnotationRegistry.resolve(annotation, context);
        }

        context = this.annotationCommandEditorRegistry.edit(instance, context);

        return context;
    }

}
