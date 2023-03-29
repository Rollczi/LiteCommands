package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.meta.CommandMetaHolder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionsExcluded {

    PermissionExcluded[] value();

    class AnnotationResolver<SENDER> implements CommandAnnotationMetaApplicator<SENDER, PermissionsExcluded> {

        @Override
        public void apply(Object instance, PermissionsExcluded annotation, CommandMetaHolder metaHolder) {
            metaHolder.editMeta(meta -> {
                for (PermissionExcluded permissionAnnotation : annotation.value()) {
                    meta.listEditor(CommandMeta.PERMISSIONS_EXCLUDED)
                        .addAll(permissionAnnotation.value())
                        .apply();
                }
            });
        }

    }

}
