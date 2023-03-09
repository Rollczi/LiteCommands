package dev.rollczi.litecommands.modern.annotation.permission;

import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.modern.meta.CommandMeta;
import dev.rollczi.litecommands.modern.meta.CommandMetaHolder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permissions {

    Permission[] value();

    class AnnotationResolver<SENDER> implements CommandAnnotationMetaApplicator<SENDER, Permissions> {

        @Override
        public void apply(Object instance, Permissions annotation, CommandMetaHolder metaHolder) {
            metaHolder.editMeta(meta -> {
                for (Permission permissionAnnotation : annotation.value()) {
                    for (String permission : permissionAnnotation.value()) {
                        meta.appendToList(CommandMeta.PERMISSIONS, permission);
                    }
                }
            });
        }

    }
}
