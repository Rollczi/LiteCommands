package dev.rollczi.litecommands.modern.annotation.argument;

import dev.rollczi.litecommands.modern.annotation.command.ArgumentAnnotationResolver;
import dev.rollczi.litecommands.modern.annotation.command.ParameterPreparedArgument;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Arg {

}
