package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;
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
            return invoker.on(Permission.class, (annotation, metaHolder) -> {
                metaHolder.meta().listEditor(Meta.PERMISSIONS)
                    .addAll(annotation.value())
                    .apply();
            });
        }
    }
}

