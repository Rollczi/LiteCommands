package dev.rollczi.litecommands.extension.annotations;

import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.annotations.validator.requirment.AnnotatedValidator;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.Annotation;

@ApiStatus.Experimental
public interface AnnotationsExtension<SENDER> {

    LiteAnnotationsProcessorExtension<SENDER> processor(AnnotationProcessor<SENDER> processor);

    <T, A extends Annotation> LiteAnnotationsProcessorExtension<SENDER> validator(Class<T> type, Class<A> annotation, AnnotatedValidator<SENDER, T, A> validator);

}
