package dev.rollczi.litecommands.annotations.bind;

import dev.rollczi.litecommands.annotations.requirement.RequirementDefinition;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@RequirementDefinition(type = RequirementDefinition.Type.BIND)
public @interface Bind {
}
