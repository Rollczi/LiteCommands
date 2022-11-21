package dev.rollczi.litecommands.command.amount;

import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface Max {

    int value();

    FactoryAnnotationResolver<Max> RESOLVER = FactoryAnnotationResolver.of(Max.class, (max, state) -> state.validator(validator -> validator.max(max.value())));

}
