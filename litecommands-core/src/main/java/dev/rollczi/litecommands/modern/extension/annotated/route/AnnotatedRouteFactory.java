package dev.rollczi.litecommands.modern.extension.annotated.route;

import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorContextStructurePiece;

import java.lang.annotation.Annotation;

public class AnnotatedRouteFactory {

    private final AnnotatedRouteService annotatedRouteService;

    public AnnotatedRouteFactory(AnnotatedRouteService annotatedRouteService) {
        this.annotatedRouteService = annotatedRouteService;
    }

    public CommandRoute createRoute(Object instance) {
        Class<?> type = instance.getClass();
        CommandEditorContextStructurePiece context = new CommandEditorContextStructurePiece();

        for (Annotation annotation : type.getAnnotations()) {
            annotatedRouteService.resolve(annotation, context);
        }

        return context.buildRoute();
    }



}
