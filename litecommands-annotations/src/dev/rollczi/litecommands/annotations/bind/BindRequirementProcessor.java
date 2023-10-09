package dev.rollczi.litecommands.annotations.bind;

import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.requirement.BindRequirement;
import dev.rollczi.litecommands.requirement.Requirement;

public class BindRequirementProcessor<SENDER> extends RequirementProcessor<SENDER, Bind> {

    public BindRequirementProcessor() {
        super(Bind.class);
    }

    @Override
    public <PARSED> Requirement<PARSED> create(AnnotationHolder<Bind, PARSED, ?> holder) {
        return BindRequirement.of(() -> holder.getName(), holder.getFormat());
    }

}
