package dev.rollczi.litecommands.annotations.context;

import dev.rollczi.litecommands.annotations.requirement.RequirementDefinition;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides the context for a command invocation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@RequirementDefinition(type = RequirementDefinition.Type.CONTEXT)
public @interface Context {

}
