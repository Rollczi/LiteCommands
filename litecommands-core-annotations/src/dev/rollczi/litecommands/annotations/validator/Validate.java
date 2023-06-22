package dev.rollczi.litecommands.annotations.validator;

import dev.rollczi.litecommands.annotations.processor.CommandAnnotationMetaApplicator;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.validator.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {

    Class<? extends Validator<?>>[] value();

    class AnnotationResolver<SENDER> implements CommandAnnotationMetaApplicator<SENDER, Validate> {

        @Override
        public void apply(Object instance, Validate annotation, MetaHolder metaHolder) {
            metaHolder.meta().listEditor(Meta.VALIDATORS)
                .addAll(annotation.value())
                .apply();
        }

    }

}
