package dev.rollczi.litecommands.modern.annotation.processor;

import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CommandAnnotationRegistry<SENDER> {

    private final Map<Class<? extends Annotation>, CommandAnnotationResolver<SENDER, ?>> resolvers = new HashMap<>();
    private final Map<Class<? extends Annotation>, CommandAnnotationMethodResolver<SENDER, ?>> methodResolvers = new HashMap<>();

    public <A extends Annotation> void registerResolver(Class<A> annotation, CommandAnnotationResolver<SENDER, A> resolver) {
        this.resolvers.put(annotation, resolver);
    }

    public <A extends Annotation> void registerMethodResolver(Class<A> annotation, CommandAnnotationMethodResolver<SENDER, A> resolver) {
        this.methodResolvers.put(annotation, resolver);
    }

    public <A extends Annotation> void replaceResolver(Class<A> annotation, Function<CommandAnnotationResolver<SENDER, A>, CommandAnnotationResolver<SENDER, A>> resolver) {
        this.resolvers.replace(annotation, resolver.apply(this.getResolver(annotation)));
    }

    public <A extends Annotation> void replaceMethodResolver(Class<A> annotation, Function<CommandAnnotationMethodResolver<SENDER, A>, CommandAnnotationMethodResolver<SENDER, A>> resolver) {
        this.methodResolvers.replace(annotation, resolver.apply(this.getMethodResolver(annotation)));
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> CommandEditorContext<SENDER> resolve(Object instance, A annotation, CommandEditorContext<SENDER> context) {
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        CommandAnnotationResolver<SENDER, A> resolver = this.getResolver(annotationType);

        if (resolver != null) {
            return resolver.resolve(instance, annotation, context);
        }

        return context;
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation> CommandAnnotationResolver<SENDER, A> getResolver(Class<A> annotation) {
        return (CommandAnnotationResolver<SENDER, A>) this.resolvers.get(annotation);
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> CommandEditorContext<SENDER> resolve(Object instance, Method method, A annotation, CommandEditorContext<SENDER> context) {
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        CommandAnnotationMethodResolver<SENDER, A> resolver = this.getMethodResolver(annotationType);

        if (resolver != null) {
            return resolver.resolve(instance, method, annotation, context);
        }

        return context;
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation> CommandAnnotationMethodResolver<SENDER, A> getMethodResolver(Class<A> annotation) {
        return (CommandAnnotationMethodResolver<SENDER, A>) this.methodResolvers.get(annotation);
    }

}
