package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Permissions {

    Permission[] value();

    class AnnotationResolver<SENDER> implements CommandAnnotationMetaApplicator<SENDER, Permissions> {

        @Override
        public void apply(Object instance, Permissions annotation, MetaHolder metaHolder) {
            Meta meta = metaHolder.meta();

            for (Permission permissionAnnotation : annotation.value()) {
                meta.listEditor(Meta.PERMISSIONS)
                    .addAll(permissionAnnotation.value())
                    .apply();
            }
        }

    }

}