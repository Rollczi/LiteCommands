package dev.rollczi.litecommands.factory;

import panda.std.Option;

import java.lang.annotation.Annotation;
import java.util.function.BiFunction;

public interface FactoryAnnotationResolver<A extends Annotation> {

    Option<CommandState> resolve(A annotation, CommandState state);

    default Option<CommandState> tryResolve(Annotation annotation, CommandState state) {
        return resolve(this.getAnnotationClass().cast(annotation), state);
    }

    Class<A> getAnnotationClass();

    static <T extends Annotation> FactoryAnnotationResolver<T> of(Class<T> type, BiFunction<T, CommandState, CommandState> resolver) {
        return new SimpleAnnotationResolver<T>(type, (t, commandState) -> Option.of(resolver.apply(t, commandState)));
    }

    static <T extends Annotation> FactoryAnnotationResolver<T> ofOption(Class<T> type, BiFunction<T, CommandState, Option<CommandState>> resolver) {
        return new SimpleAnnotationResolver<T>(type, resolver);
    }

}
