package dev.rollczi.litecommands.annotations.literal;

import dev.rollczi.litecommands.annotations.requirement.RequirementDefinition;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.ApiStatus;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@RequirementDefinition(type = RequirementDefinition.Type.ARGUMENT, nameProviders = { "value" })
@ApiStatus.Experimental
public @interface Literal {

    String[] value();

    boolean ignoreCase() default false;

}
