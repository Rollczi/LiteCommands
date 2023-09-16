package dev.rollczi.litecommands.flag;

import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.argument.SimpleArgument;

import java.util.Optional;

class FlagArgument extends SimpleArgument<Flag, Boolean> {

    FlagArgument(AnnotationHolder<Flag, Boolean, ?> holder) {
        super(holder);
    }

    @Override
    public String getName() {
        return getAnnotation().value();
    }

    @Override
    public Optional<Boolean> defaultValue() {
        return Optional.of(Boolean.FALSE);
    }

}
