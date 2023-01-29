package dev.rollczi.litecommands.modern.annotation.processor;

import dev.rollczi.litecommands.modern.annotation.editor.AnnotationCommandEditorService;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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
            context = this.commandAnnotationRegistry.resolve(instance, annotation, context);
        }

        for (Method method : type.getDeclaredMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                context = this.commandAnnotationRegistry.resolve(instance, annotation, context);
                context = this.commandAnnotationRegistry.resolve(instance, method, annotation, context);
            }
        }

        context = this.annotationCommandEditorRegistry.edit(instance, context);

        return context;
    }

}
