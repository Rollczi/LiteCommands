package dev.rollczi.litecommands.annotation;

import dev.rollczi.litecommands.requirement.Requirement;

import java.lang.annotation.Annotation;

interface RequirementFactory<A extends Annotation> {

    <PARSED> Requirement<PARSED> create(AnnotationHolder<A, PARSED, ?> holder);

}
