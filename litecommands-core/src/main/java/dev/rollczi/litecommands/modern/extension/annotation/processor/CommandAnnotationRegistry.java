package dev.rollczi.litecommands.modern.extension.annotation.processor;

import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CommandAnnotationRegistry {

    private final Map<Class<? extends Annotation>, CommandAnnotationResolver<?>> resolvers = new HashMap<>();

    public <A extends Annotation> void registerResolver(Class<A> annotation, CommandAnnotationResolver<A> resolver) {
        this.resolvers.put(annotation, resolver);
    }

    public <A extends Annotation> void replaceResolver(Class<A> annotation, Function<CommandAnnotationResolver<A>, CommandAnnotationResolver<A>> resolver) {
        this.resolvers.replace(annotation, resolver.apply(this.getResolver(annotation)));
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> CommandEditorContext resolve(A annotation, CommandEditorContext context) {
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        CommandAnnotationResolver<A> resolver = this.getResolver(annotationType);

        if (resolver != null) {
            return resolver.process(annotation, context);
        }

        return context;
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation> CommandAnnotationResolver<A> getResolver(Class<A> annotation) {
        return (CommandAnnotationResolver<A>) this.resolvers.get(annotation);
    }

}
