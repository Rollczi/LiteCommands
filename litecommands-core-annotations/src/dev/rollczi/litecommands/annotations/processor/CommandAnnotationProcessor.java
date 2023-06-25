package dev.rollczi.litecommands.annotations.processor;

import dev.rollczi.litecommands.annotations.editor.AnnotationEditorService;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.command.builder.CommandBuilderExecutor;
import dev.rollczi.litecommands.editor.EditorService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CommandAnnotationProcessor<SENDER> {

    private final CommandAnnotationRegistry<SENDER> commandAnnotationRegistry;
    private final AnnotationEditorService<SENDER> annotationCommandEditorRegistry;
    private final EditorService<SENDER> editorService;

    public CommandAnnotationProcessor(CommandAnnotationRegistry<SENDER> commandAnnotationRegistry, AnnotationEditorService<SENDER> annotationCommandEditorRegistry, EditorService<SENDER> editorService) {
        this.commandAnnotationRegistry = commandAnnotationRegistry;
        this.annotationCommandEditorRegistry = annotationCommandEditorRegistry;
        this.editorService = editorService;
    }

    public CommandBuilder<SENDER> createContext(Object instance) {
        Class<?> type = instance.getClass();
        CommandBuilder<SENDER> context = CommandBuilder.create();

        for (Annotation annotation : type.getAnnotations()) {
            context = this.commandAnnotationRegistry.resolve(instance, annotation, context);
        }

        for (Method method : type.getDeclaredMethods()) {
            CommandBuilderExecutor<SENDER> executorBuilder = new CommandBuilderExecutor<>(context);

            for (Annotation annotation : method.getAnnotations()) {
                context = this.commandAnnotationRegistry.resolve(instance, method, annotation, context, executorBuilder);
            }
        }

        context = this.annotationCommandEditorRegistry.edit(instance.getClass(), context);
        context = this.editorService.edit(context);

        return context;
    }

}
