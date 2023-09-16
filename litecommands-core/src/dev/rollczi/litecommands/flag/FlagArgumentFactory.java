package dev.rollczi.litecommands.flag;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.argument.ArgumentFactory;
import dev.rollczi.litecommands.wrapper.WrapFormat;

public class FlagArgumentFactory implements ArgumentFactory<Flag> {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Argument<T> create(AnnotationHolder<Flag, T, ?> holder) {
        WrapFormat<T, ?> wrapFormat = holder.getFormat();
        Class<T> parsedType = wrapFormat.getParsedType();

        if (parsedType != Boolean.class && parsedType != boolean.class) {
            throw new IllegalArgumentException("Flag argument must be boolean");
        }

        AnnotationHolder<Flag, Boolean, ?> flagHolder = (AnnotationHolder<Flag, Boolean, ?>) holder;

        return (Argument<T>) new FlagArgument(flagHolder);
    }

}
