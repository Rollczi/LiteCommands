package dev.rollczi.litecommands.command.amount;

import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface Required {

    int value();

    FactoryAnnotationResolver<Required> RESOLVER = FactoryAnnotationResolver.of(
            Required.class,
            (required, state) -> state.validator(validator -> validator.required(required.value()))
    );


}
