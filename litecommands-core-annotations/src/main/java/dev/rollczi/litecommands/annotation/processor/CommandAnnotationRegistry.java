package dev.rollczi.litecommands.annotation.processor;

import dev.rollczi.litecommands.editor.CommandEditorContext;
import dev.rollczi.litecommands.editor.CommandEditorExecutorBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CommandAnnotationRegistry<SENDER> {

    private final Map<Class<? extends Annotation>, CommandAnnotationClassResolver<SENDER, ?>> resolvers = new HashMap<>();
    private final Map<Class<? extends Annotation>, CommandAnnotationMethodResolver<SENDER, ?>> methodResolvers = new HashMap<>();

    public <A extends Annotation> void registerResolver(Class<A> annotation, CommandAnnotationClassResolver<SENDER, A> resolver) {
        this.resolvers.put(annotation, resolver);
    }

    public <A extends Annotation> void registerMethodResolver(Class<A> annotation, CommandAnnotationMethodResolver<SENDER, A> resolver) {
        this.methodResolvers.put(annotation, resolver);
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> CommandEditorContext<SENDER> resolve(Object instance, A annotation, CommandEditorContext<SENDER> context) {
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        CommandAnnotationClassResolver<SENDER, A> resolver = this.getResolver(annotationType);

        if (resolver != null) {
            return resolver.resolve(instance, annotation, context);
        }

        return context;
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation> CommandAnnotationClassResolver<SENDER, A> getResolver(Class<A> annotation) {
        return (CommandAnnotationClassResolver<SENDER, A>) this.resolvers.get(annotation);
    }

    @SuppressWarnings("unchecked")
    public <A extends Annotation> CommandEditorContext<SENDER> resolve(Object instance, Method method, A annotation, CommandEditorContext<SENDER> context, CommandEditorExecutorBuilder<SENDER> executorBuilder) {
        Class<A> annotationType = (Class<A>) annotation.annotationType();
        CommandAnnotationMethodResolver<SENDER, A> resolver = this.getMethodResolver(annotationType);

        if (resolver != null) {
            return resolver.resolve(instance, method, annotation, context, executorBuilder);
        }

        return context;
    }

    @SuppressWarnings("unchecked")
    private <A extends Annotation> CommandAnnotationMethodResolver<SENDER, A> getMethodResolver(Class<A> annotation) {
        return (CommandAnnotationMethodResolver<SENDER, A>) this.methodResolvers.get(annotation);
    }

}
