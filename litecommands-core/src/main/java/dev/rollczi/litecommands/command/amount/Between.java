package dev.rollczi.litecommands.command.amount;

import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface Between {

    int min();

    int max();

    FactoryAnnotationResolver<Between> RESOLVER = FactoryAnnotationResolver.of(
            Between.class,
            (between, state) -> state.validator(validator -> validator
                    .min(between.min())
                    .max(between.max())
            )
    );

}
