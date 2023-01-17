package dev.rollczi.litecommands.modern.extension.annotated.route;

import dev.rollczi.litecommands.modern.command.editor.CommandEditorContextStructurePiece;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AnnotatedRouteService {

    private final Map<Class<? extends Annotation>, AnnotatedRouteResolver> resolvers = new HashMap<>();

    public void registerResolver(Class<? extends Annotation> annotation, AnnotatedRouteResolver resolver) {
        resolvers.put(annotation, resolver);
    }

    public void replaceResolver(Class<? extends Annotation> annotation, Function<AnnotatedRouteResolver, AnnotatedRouteResolver> resolver) {
        resolvers.replace(annotation, resolver.apply(resolvers.get(annotation)));
    }

    public void resolve(Annotation annotation, CommandEditorContextStructurePiece context) {
        AnnotatedRouteResolver resolver = resolvers.get(annotation.annotationType());

        if (resolver != null) {
            resolver.resolve(annotation, context);
        }
    }

}
