package dev.rollczi.litecommands.modern.extension.annotation.analyzer;

import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;

import java.lang.annotation.Annotation;

public class CommandAnnotationProcessor {

    private final CommandAnnotationRegistry commandAnnotationRegistry;

    public CommandAnnotationProcessor(CommandAnnotationRegistry commandAnnotationRegistry) {
        this.commandAnnotationRegistry = commandAnnotationRegistry;
    }

    public CommandRoute createRoute(Object instance) {
        Class<?> type = instance.getClass();
        CommandEditorContext context = CommandEditorContext.empty();

        for (Annotation annotation : type.getAnnotations()) {
            context = commandAnnotationRegistry.resolve(annotation, context);
        }


    }

}
