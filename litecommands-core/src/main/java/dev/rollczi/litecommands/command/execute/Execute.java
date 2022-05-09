package dev.rollczi.litecommands.command.execute;

import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Execute {

    String route() default "";

    String[] aliases() default {};

    int required() default -1;

    int min() default -1;

    int max() default -1;

    FactoryAnnotationResolver<Execute> RESOLVER = new ExecuteAnnotationResolver();

}
