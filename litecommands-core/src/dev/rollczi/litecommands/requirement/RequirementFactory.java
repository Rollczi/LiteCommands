package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.annotation.AnnotationHolder;

import java.lang.annotation.Annotation;

public interface RequirementFactory<SENDER, A extends Annotation> {

    <PARSED> Requirement<SENDER, PARSED> create(AnnotationHolder<A, PARSED, ?> holder);

}
