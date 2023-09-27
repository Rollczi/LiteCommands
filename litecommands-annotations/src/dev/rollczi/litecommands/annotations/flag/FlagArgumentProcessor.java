package dev.rollczi.litecommands.annotations.flag;

import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.flag.FlagArgument;
import dev.rollczi.litecommands.wrapper.WrapFormat;

public class FlagArgumentProcessor<SENDER> extends RequirementProcessor<SENDER, Flag> {

    public FlagArgumentProcessor() {
        super(Flag.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Argument<T> create(AnnotationHolder<Flag, T, ?> holder) {
        WrapFormat<T, ?> wrapFormat = holder.getFormat();
        Class<T> parsedType = wrapFormat.getParsedType();

        if (parsedType != Boolean.class && parsedType != boolean.class) {
            throw new IllegalArgumentException("Flag argument must be boolean");
        }

        AnnotationHolder<Flag, Boolean, ?> flagHolder = (AnnotationHolder<Flag, Boolean, ?>) holder;
        Flag flag = holder.getAnnotation();

        return (Argument<T>) new FlagArgument(flag.value(), flagHolder.getFormat());
    }

}
