package dev.rollczi.litecommands.meta;

import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MarkMeta {

    String key();

    String value();

    class AnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

        @Override
        public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
            return invoker.on(MarkMeta.class, (annotation, metaHolder) -> {
                metaHolder.meta().put(MetaKey.of(annotation.key(), String.class), annotation.value());
            });
        }
    }
}

