package dev.rollczi.litecommands.modern.annotation.permission;

import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.modern.meta.CommandMeta;
import dev.rollczi.litecommands.modern.meta.CommandMetaHolder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Permissions.class)
public @interface Permission {

    String[] value();

    class AnnotationResolver<SENDER> implements CommandAnnotationMetaApplicator<SENDER, Permission> {

        @Override
        public void apply(Object instance, Permission annotation, CommandMetaHolder metaHolder) {
            metaHolder.editMeta(meta -> {
                for (String permission : annotation.value()) {
                    meta.appendToList(CommandMeta.PERMISSIONS, permission);
                }
            });
        }

    }
}
