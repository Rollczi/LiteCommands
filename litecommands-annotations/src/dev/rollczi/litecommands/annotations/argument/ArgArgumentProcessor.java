package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.SimpleArgument;

public class ArgArgumentProcessor<SENDER> extends RequirementProcessor<SENDER, Arg> {

    public ArgArgumentProcessor() {
        super(Arg.class);
    }

    @Override
    public <T> Argument<T> create(AnnotationHolder<Arg, T, ?> holder) {
        Arg annotation = holder.getAnnotation();

        return new SimpleArgument<>(() -> {
            String name = annotation.value();

            if (!name.isEmpty()) {
                return name;
            }

            return holder.getName();
        }, holder.getFormat());
    }
}
