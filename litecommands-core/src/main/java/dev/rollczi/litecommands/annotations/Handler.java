package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.argument.ArgumentHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Handler {

    Class<? extends ArgumentHandler> value() default ArgumentHandler.class;

}
