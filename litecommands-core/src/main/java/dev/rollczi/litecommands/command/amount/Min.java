package dev.rollczi.litecommands.command.amount;

import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Min {

    int value();

    FactoryAnnotationResolver<Min> RESOLVER = FactoryAnnotationResolver.of(Min.class, (min, state) -> state.validator(validator -> validator.min(min.value())));

}
