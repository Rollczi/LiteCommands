package dev.rollczi.litecommands.annotations.validator.requirment;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;

import java.lang.annotation.Annotation;

public class AnnotatedValidatorProcessor<SENDER, T, A extends Annotation> implements AnnotationProcessor<SENDER> {

    private final Class<A> annotationClass;
    private final Class<T> type;
    private final AnnotatedValidator<SENDER, T, A> annotatedValidator;

    public AnnotatedValidatorProcessor(Class<A> annotationClass, Class<T> type, AnnotatedValidator<SENDER, T, A>  annotatedValidator) {
        this.annotationClass = annotationClass;
        this.type = type;
        this.annotatedValidator = annotatedValidator;
    }

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onParameterRequirement(annotationClass, (parameter, annotationHolder, builder, requirement) -> {
            Class<?> parsedType = requirement.getType().getRawType();

            if (!type.isAssignableFrom(parsedType)) {
                return;
            }

            RequirementAnnotatedValidatorImpl<SENDER, T, A> validator = new RequirementAnnotatedValidatorImpl<>(annotatedValidator, annotationHolder.getAnnotation());

            requirement.meta().listEditor(Meta.REQUIREMENT_VALIDATORS)
                .add(validator)
                .apply();
        });
    }

}
