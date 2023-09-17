package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;
import dev.rollczi.litecommands.meta.Meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permissions {

    Permission[] value();

    class AnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

        @Override
        public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
            return invoker.on(Permissions.class, (annotation, metaHolder) -> {
                Meta meta = metaHolder.meta();

                for (Permission permissionAnnotation : annotation.value()) {
                    meta.listEditor(Meta.PERMISSIONS)
                        .addAll(permissionAnnotation.value())
                        .apply();
                }
            });
        }

    }
}
