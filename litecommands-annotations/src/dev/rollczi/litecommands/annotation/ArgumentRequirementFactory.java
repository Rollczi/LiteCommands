package dev.rollczi.litecommands.annotation;

import dev.rollczi.litecommands.requirement.Requirement;

import java.lang.annotation.Annotation;

class ArgumentRequirementFactory<SENDER, A extends Annotation> implements RequirementFactory<A> {

    private final ArgumentFactory<A> argumentFactory;

    public ArgumentRequirementFactory(ArgumentFactory<A> argumentFactory) {
        this.argumentFactory = argumentFactory;
    }

    @Override
    public <PARSED> Requirement<PARSED> create(AnnotationHolder<A, PARSED, ?> holder) {
        return argumentFactory.create(holder);
    }

}
