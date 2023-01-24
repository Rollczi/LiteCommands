package dev.rollczi.litecommands.modern.annotation.argument.literal;

import dev.rollczi.litecommands.modern.annotation.argument.ArgumentAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ArgumentAnnotation
public @interface Literal {

    String value();

}
