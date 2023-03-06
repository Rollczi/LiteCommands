package dev.rollczi.litecommands.modern.permission.annotation;

import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.modern.meta.CommandMeta;
import dev.rollczi.litecommands.modern.meta.CommandMetaHolder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionsExcluded {

    PermissionExcluded[] value();

    class AnnotationResolver<SENDER> implements CommandAnnotationMetaApplicator<SENDER, PermissionsExcluded> {

        @Override
        public void apply(Object instance, PermissionsExcluded annotation, CommandMetaHolder metaHolder) {
            CommandMeta meta = metaHolder.getMeta();

            for (PermissionExcluded permissionAnnotation : annotation.value()) {
                for (String permission : permissionAnnotation.value()) {
                    meta.appendToList(CommandMeta.PERMISSIONS_EXCLUDED, permission);
                }
            }
        }

    }

}
