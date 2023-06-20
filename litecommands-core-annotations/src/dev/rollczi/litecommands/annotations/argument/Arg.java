package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.command.requirement.RequirementAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@RequirementAnnotation
public @interface Arg {

    String value() default "";

}
