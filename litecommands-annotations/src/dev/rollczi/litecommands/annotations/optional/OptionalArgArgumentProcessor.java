package dev.rollczi.litecommands.annotations.optional;

import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.SimpleArgument;

public class OptionalArgArgumentProcessor<SENDER> extends RequirementProcessor<SENDER, OptionalArg> {

    public OptionalArgArgumentProcessor() {
        super(OptionalArg.class);
    }

    @Override
    public <T> Argument<T> create(AnnotationHolder<OptionalArg, T, ?> holder) {
        OptionalArg annotation = holder.getAnnotation();

        String name = annotation.value();

        if (name.isEmpty()) {
            name = holder.getName();
        }

        return new SimpleArgument<>(name, holder.getFormat(), true);
    }
}
