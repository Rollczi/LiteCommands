package dev.rollczi.litecommands.annotations.optional;

import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.requirement.Requirement;

public class OptionalArgArgumentProcessor<SENDER> extends RequirementProcessor<SENDER, OptionalArg> {

    public OptionalArgArgumentProcessor() {
        super(OptionalArg.class);
    }

    @Override
    protected Requirement<?> create(AnnotationHolder<OptionalArg, ?> holder) {
        OptionalArg annotation = holder.getAnnotation();

        String name = annotation.value();

        if (name.isEmpty()) {
            name = holder.getName();
        }

        return Argument.of(name, holder.getType(), true);
    }

}
