package dev.rollczi.litecommands.annotations.context;

import dev.rollczi.litecommands.annotations.command.requirement.RequirementAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@RequirementAnnotation
public @interface Context {
}
