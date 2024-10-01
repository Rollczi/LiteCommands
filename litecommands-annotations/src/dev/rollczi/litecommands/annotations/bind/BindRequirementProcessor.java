package dev.rollczi.litecommands.annotations.bind;

import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.annotations.requirement.RequirementProcessor;
import dev.rollczi.litecommands.bind.BindRequirement;
import dev.rollczi.litecommands.requirement.Requirement;

public class BindRequirementProcessor<SENDER> extends RequirementProcessor<SENDER, Bind> {

    public BindRequirementProcessor() {
        super(Bind.class);
    }

    @Override
    public Requirement<?> create(AnnotationHolder<Bind, ?> holder) {
        return BindRequirement.of(() -> holder.getName(), holder.getType());
    }

}
