package dev.rollczi.litecommands.annotations.meta;

import dev.rollczi.litecommands.annotations.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.meta.MetaKey;
import dev.rollczi.litecommands.meta.MetaHolder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Meta {

    String key();

    String value();

    class AnnotationResolver<SENDER> implements CommandAnnotationMetaApplicator<SENDER, Meta> {
        @Override
        public void apply(Object instance, Meta annotation, MetaHolder metaHolder) {
            metaHolder.editMeta(meta -> meta.put(MetaKey.of(annotation.key(), String.class), annotation.value()));
        }
    }

}
