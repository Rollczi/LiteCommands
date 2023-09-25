package dev.rollczi.litecommands.annotations.requirement;

import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.requirement.Requirement;

import java.lang.annotation.Annotation;
import java.util.Optional;

public abstract class RequirementProcessor<SENDER, A extends Annotation> implements AnnotationProcessor<SENDER> {

    private final Class<A> annotationClass;

    public RequirementProcessor(Class<A> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onRequirement(annotationClass, (holder, builder) -> Optional.of(create(holder)));
    }

    abstract protected <PARSED> Requirement<PARSED> create(AnnotationHolder<A, PARSED, ?> holder);

}
