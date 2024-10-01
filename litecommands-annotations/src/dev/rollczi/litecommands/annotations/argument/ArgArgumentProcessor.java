package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.requirement.Requirement;

public class ArgArgumentProcessor<SENDER> extends RequirementProcessor<SENDER, Arg> {

    public ArgArgumentProcessor() {
        super(Arg.class);
    }

    @Override
    protected Requirement<?> create(AnnotationHolder<Arg, ?> holder) {
        Arg annotation = holder.getAnnotation();

        String name = annotation.value();

        if (name.isEmpty()) {
            name = holder.getName();
        }

        return Argument.of(name, holder.getType());
    }
}
