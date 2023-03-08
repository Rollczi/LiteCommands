package dev.rollczi.litecommands.modern.permission.annotation;

import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.modern.meta.CommandMeta;
import dev.rollczi.litecommands.modern.meta.CommandMetaHolder;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(PermissionsExcluded.class)
public @interface PermissionExcluded {

    String[] value();

    class AnnotationResolver<SENDER> implements CommandAnnotationMetaApplicator<SENDER, PermissionExcluded> {

        @Override
        public void apply(Object instance, PermissionExcluded annotation, CommandMetaHolder metaHolder) {
            metaHolder.editMeta(meta -> {
                for (String permission : annotation.value()) {
                    meta.appendToList(CommandMeta.PERMISSIONS_EXCLUDED, permission);
                }
            });
        }

    }
}
