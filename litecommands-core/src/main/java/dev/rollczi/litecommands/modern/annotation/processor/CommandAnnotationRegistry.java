package dev.rollczi.litecommands.modern.annotation.processor;

import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CommandAnnotationRegistry {

    private final Map<Class<? extends Annotation>, CommandAnnotationResolver<?>> resolvers = new HashMap<>();
    private final Map<Class<? extends Annotation>, CommandAnnotationMethodResolver<?>> methodResolvers = new HashMap<>();

    public <A extends Annotation> void registerResolver(Class<A> annotation, CommandAnnotationResolver<A> resolver) {
        this.resolvers.put(annotation, resolver);
    }

    public <A extends Annotation> void registerMethodResolver(Class<A> annotation, CommandAnnotationMethodResolver<A> resolver) {
        this.methodResolvers.put(annotation, resolver);
    }

    public <A extends Annotation> void replaceResolver(Class<A> annotation, Function<CommandAnnotationResolver<A>, CommandAnnotationResolver<A>> resolver) {
        this.resolvers.replace(annotation, resolver.apply(this.getResolver(annotation)));
    }

    public <A extends Annotation> void replaceMethodResolver(Class<A> annotation, Function<CommandAnnotationMethodResolver<A>, CommandAnnotationMethodResolver<A>> resolver) {
        this.methodResolvers.replace(annotation, resolver.apply(this.getMethodResolver(annotation)));
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> CommandEditorContext resolve(Object instance, A annotation, CommandEditorContext context) {
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        CommandAnnotationResolver<A> resolver = this.getResolver(annotationType);

        if (resolver != null) {
            return resolver.resolve(instance, annotation, context);
        }

        return context;
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation> CommandAnnotationResolver<A> getResolver(Class<A> annotation) {
        return (CommandAnnotationResolver<A>) this.resolvers.get(annotation);
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> CommandEditorContext resolve(Object instance, Method method, A annotation, CommandEditorContext context) {
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        CommandAnnotationMethodResolver<A> resolver = this.getMethodResolver(annotationType);

        if (resolver != null) {
            return resolver.resolve(instance, method, annotation, context);
        }

        return context;
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation> CommandAnnotationMethodResolver<A> getMethodResolver(Class<A> annotation) {
        return (CommandAnnotationMethodResolver<A>) this.methodResolvers.get(annotation);
    }

}
