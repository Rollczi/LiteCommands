package dev.rollczi.litecommands.command.async;

import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Async {

    FactoryAnnotationResolver<Async> RESOLVER = new AsyncAnnotationResolver();

}
