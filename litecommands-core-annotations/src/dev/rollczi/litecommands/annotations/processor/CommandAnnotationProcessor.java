package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.annotations.editor.AnnotationCommandEditorService;
import dev.rollczi.litecommands.editor.CommandEditorContext;
import dev.rollczi.litecommands.editor.CommandEditorExecutorBuilder;
import dev.rollczi.litecommands.editor.CommandEditorService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CommandAnnotationProcessor<SENDER> {

    private final CommandAnnotationRegistry<SENDER> commandAnnotationRegistry;
    private final AnnotationCommandEditorService<SENDER> annotationCommandEditorRegistry;
    private final CommandEditorService<SENDER> commandEditorService;

    public CommandAnnotationProcessor(CommandAnnotationRegistry<SENDER> commandAnnotationRegistry, AnnotationCommandEditorService<SENDER> annotationCommandEditorRegistry, CommandEditorService<SENDER> commandEditorService) {
        this.commandAnnotationRegistry = commandAnnotationRegistry;
        this.annotationCommandEditorRegistry = annotationCommandEditorRegistry;
        this.commandEditorService = commandEditorService;
    }

    public CommandEditorContext<SENDER> createContext(Object instance) {
        Class<?> type = instance.getClass();
        CommandEditorContext<SENDER> context = CommandEditorContext.create();

        for (Annotation annotation : type.getAnnotations()) {
            context = this.commandAnnotationRegistry.resolve(instance, annotation, context);
        }

        for (Method method : type.getDeclaredMethods()) {
            CommandEditorExecutorBuilder<SENDER> executorBuilder = new CommandEditorExecutorBuilder<>();

            for (Annotation annotation : method.getAnnotations()) {
                context = this.commandAnnotationRegistry.resolve(instance, method, annotation, context, executorBuilder);
            }
        }

        context = this.annotationCommandEditorRegistry.edit(instance.getClass(), context);
        context = this.commandEditorService.edit(context);

        return context;
    }

}
