package dev.rollczi.litecommands.annotations.join;

import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.join.JoinProfile;
import dev.rollczi.litecommands.requirement.Requirement;

public class JoinArgumentProcessor<SENDER> extends RequirementProcessor<SENDER, Join> {

    public JoinArgumentProcessor() {
        super(Join.class);
    }

    @Override
    protected Requirement<?> create(AnnotationHolder<Join, ?> holder) {
        Join annotation = holder.getAnnotation();
        String name = annotation.value();

        if (name.isEmpty()) {
            name = holder.getName();
        }

        return Argument.profiled(name, holder.getType(), new JoinProfile(annotation.separator(), annotation.limit()));
    }

}
