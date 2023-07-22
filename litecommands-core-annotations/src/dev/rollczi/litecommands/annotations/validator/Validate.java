package dev.rollczi.litecommands.annotations.validator;

import dev.rollczi.litecommands.annotations.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.processor.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.validator.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {

    Class<? extends Validator<?>>[] value();

    class AnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

        @Override
        public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
            return invoker.onAnnotatedMetaHolder(Validate.class, (instance, annotation, metaHolder) -> {
                metaHolder.meta().listEditor(Meta.VALIDATORS)
                    .addAll(annotation.value())
                    .apply();
            });
        }
    }

}

