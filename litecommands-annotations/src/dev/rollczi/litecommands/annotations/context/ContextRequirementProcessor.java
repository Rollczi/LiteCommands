package dev.rollczi.litecommands.annotations.context;

import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.context.ContextRequirement;
import dev.rollczi.litecommands.requirement.Requirement;

public class ContextRequirementProcessor<SENDER> extends RequirementProcessor<SENDER, Context> {

    public ContextRequirementProcessor() {
        super(Context.class);
    }

    @Override
    protected Requirement<?> create(AnnotationHolder<Context, ?> holder) {
        return ContextRequirement.of(() -> holder.getName(), holder.getType());
    }

}
