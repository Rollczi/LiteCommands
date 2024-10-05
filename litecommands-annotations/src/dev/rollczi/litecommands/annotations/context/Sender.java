package dev.rollczi.litecommands.annotations.context;

import dev.rollczi.litecommands.annotations.requirement.RequirementDefinition;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Alias for {@link dev.rollczi.litecommands.annotations.context.Context}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@RequirementDefinition(type = RequirementDefinition.Type.CONTEXT)
public @interface Sender {

}
