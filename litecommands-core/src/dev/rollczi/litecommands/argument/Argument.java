package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.util.Optional;

public interface Argument<PARSED> {

    @Deprecated
    String getName();

    @Deprecated
    WrapFormat<PARSED, ?> getWrapperFormat();

    AnnotationHolder<?, PARSED, ?> getAnnotationHolder();

    default Optional<PARSED> defaultValue() {
        return Optional.empty();
    }

    default boolean hasDefaultValue() {
        return defaultValue().isPresent();
    }

    default ArgumentKey toKey() {
        return ArgumentKey.typed(this.getClass());
    }

}