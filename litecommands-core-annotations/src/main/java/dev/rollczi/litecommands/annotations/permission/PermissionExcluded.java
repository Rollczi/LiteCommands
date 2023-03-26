package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.meta.CommandMetaHolder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
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
