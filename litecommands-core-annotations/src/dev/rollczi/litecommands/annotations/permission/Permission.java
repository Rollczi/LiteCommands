package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.processor.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Permissions.class)
public @interface Permission {

    String[] value();

    class AnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

        @Override
        public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
            return invoker.onAnnotatedMetaHolder(Permission.class, (instance, annotation, metaHolder) -> {
                metaHolder.meta().listEditor(Meta.PERMISSIONS)
                    .addAll(annotation.value())
                    .apply();
            });
        }
    }
}

