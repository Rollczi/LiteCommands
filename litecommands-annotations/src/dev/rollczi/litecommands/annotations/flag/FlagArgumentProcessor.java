package dev.rollczi.litecommands.annotations.flag;

import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.flag.FlagProfile;
import dev.rollczi.litecommands.requirement.Requirement;

public class FlagArgumentProcessor<SENDER> extends RequirementProcessor<SENDER, Flag> {

    public FlagArgumentProcessor() {
        super(Flag.class);
    }

    @Override
    protected Requirement<?> create(AnnotationHolder<Flag, ?> holder) {
        Flag flag = holder.getAnnotation();

        return Argument.profiled(flag.value(), holder.getType(), new FlagProfile(flag.value()));
    }

}
