package dev.rollczi.litecommands.argument.joiner;

import dev.rollczi.litecommands.argument.ArgumentAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ArgumentAnnotation
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Joiner {

    String[] value() default {};

}
