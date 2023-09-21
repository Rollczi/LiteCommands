package dev.rollczi.litecommands.annotation;

import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;

import java.lang.annotation.Annotation;
import java.util.Optional;

public class RequirementProcessor<SENDER, A extends Annotation> implements AnnotationProcessor<SENDER> {

    private final RequirementFactory<A> requirementFactory;
    private final Class<A> annotationClass;

    public RequirementProcessor(Class<A> annotationClass, ArgumentFactory<A> argumentFactory) {
        this.requirementFactory =  new ArgumentRequirementFactory<>(argumentFactory);
        this.annotationClass = annotationClass;
    }

    public RequirementProcessor(Class<A> annotationClass) {
        this.requirementFactory = new ContextRequirementFactory<>();
        this.annotationClass = annotationClass;
    }

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onRequirement(annotationClass, (holder, builder) -> Optional.of(requirementFactory.create(holder)));
    }

}
