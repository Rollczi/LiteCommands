package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.util.Optional;

public interface Argument<PARSED> {

    String getName();

    WrapFormat<PARSED, ?> getWrapperFormat();

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