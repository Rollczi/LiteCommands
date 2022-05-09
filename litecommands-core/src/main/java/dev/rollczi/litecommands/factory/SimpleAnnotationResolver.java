package dev.rollczi.litecommands.factory;

import panda.std.Option;

import java.lang.annotation.Annotation;
import java.util.function.BiFunction;

class SimpleAnnotationResolver<A extends Annotation> implements FactoryAnnotationResolver<A> {

    private final Class<A> annotationClass;
    private final BiFunction<A, CommandState, Option<CommandState>> resolver;

    SimpleAnnotationResolver(Class<A> annotationClass, BiFunction<A, CommandState, Option<CommandState>> resolver) {
        this.annotationClass = annotationClass;
        this.resolver = resolver;
    }

    @Override
    public Class<A> getAnnotationClass() {
        return annotationClass;
    }

    @Override
    public Option<CommandState> resolve(A annotation, CommandState state) {
        if (annotationClass.isInstance(annotation)) {
            return resolver.apply(annotationClass.cast(annotation), state);
        }

        throw new IllegalArgumentException("Annotation " + annotationClass.getName() + " is not instance of " + annotation.getClass().getName());
    }

}
