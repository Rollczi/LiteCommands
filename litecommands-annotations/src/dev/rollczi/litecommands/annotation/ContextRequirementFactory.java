package dev.rollczi.litecommands.annotation;

import dev.rollczi.litecommands.requirement.Requirement;

import java.lang.annotation.Annotation;

class ContextRequirementFactory<A extends Annotation> implements RequirementFactory<A> {

    @Override
    public <PARSED> Requirement<PARSED> create(AnnotationHolder<A, PARSED, ?> holder) {
        return new ContextRequirementImpl<>(holder);
    }

}
