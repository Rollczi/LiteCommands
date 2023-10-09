package dev.rollczi.litecommands.annotations.join;

import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.join.JoinArgument;

public class JoinArgumentProcessor<SENDER> extends RequirementProcessor<SENDER, Join> {

    public JoinArgumentProcessor() {
        super(Join.class);
    }

    @Override
    public <T> Argument<T> create(AnnotationHolder<Join, T, ?> holder) {
        Join annotation = holder.getAnnotation();
        String name = annotation.value();

        if (name.isEmpty()) {
            name = holder.getName();
        }

        return new JoinArgument<>(name, holder.getFormat(), annotation.separator(), annotation.limit());
    }

}
