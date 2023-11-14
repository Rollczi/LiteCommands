package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.requirement.Requirement;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import java.lang.annotation.Annotation;
import java.util.Optional;

public interface Argument<PARSED> extends Requirement<PARSED> {

    String getName();

    WrapFormat<PARSED, ?> getWrapperFormat();

    default Optional<PARSED> defaultValue() {
        return Optional.empty();
    }

    default boolean hasDefaultValue() {
        return defaultValue().isPresent();
    }

    default ArgumentKey getKey() {
        return ArgumentKey.typed(this.getClass(), this.getKeyName());
    }

    default String getKeyName() {
        return this.meta().get(Meta.ARGUMENT_KEY, this.getName());
    }

    default Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    default <A extends Annotation> Optional<A> getAnnotation(Class<? extends Annotation> annotationClass) {
        return Optional.empty();
    }

    static <T> Argument<T> of(String name, WrapFormat<T, ?> format) {
        return new SimpleArgument<>(name, format);
    }

}