package dev.rollczi.litecommands.annotations.requirement;

import dev.rollczi.litecommands.LiteCommandsException;
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
        try {
            return invoker.onRequirement(annotationClass, (holder, builder) -> Optional.of(create(holder)));
        } catch (LiteCommandsException liteCommandsException) {
            String errorMessage = String.format("Cannot create requirement for annotation %s with processor %s",
                annotationClass.getSimpleName(),
                this.getClass().getSimpleName()
            );

            throw new LiteCommandsException(errorMessage, liteCommandsException);
        }
    }

    abstract protected <PARSED> Requirement<PARSED> create(AnnotationHolder<A, PARSED, ?> holder);

}
