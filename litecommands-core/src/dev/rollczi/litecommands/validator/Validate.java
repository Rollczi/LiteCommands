package dev.rollczi.litecommands.validator;

import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;

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
            return invoker.on(Validate.class, (annotation, metaHolder) -> {
                metaHolder.meta().listEditor(Meta.VALIDATORS)
                    .addAll(annotation.value())
                    .apply();
            });
        }
    }

}

