package dev.rollczi.litecommands.modern.extension.annotated.route;

import dev.rollczi.litecommands.modern.command.CommandRoute;

public class AnnotatedRouteFactory {

    private final AnnotatedRouteResolver annotatedRouteResolver;

    public AnnotatedRouteFactory(AnnotatedRouteResolver annotatedRouteResolver) {
        this.annotatedRouteResolver = annotatedRouteResolver;
    }

    public CommandRoute createRoute(Object instance) {
        annotatedRouteResolver
    }



}
