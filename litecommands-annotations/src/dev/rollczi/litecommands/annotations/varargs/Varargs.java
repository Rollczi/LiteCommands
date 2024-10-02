package dev.rollczi.litecommands.annotations.varargs;

import dev.rollczi.litecommands.annotations.requirement.RequirementDefinition;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@RequirementDefinition(type = RequirementDefinition.Type.ARGUMENT, nameProviders = { "value" })
public @interface Varargs {

    /**
     * Name of the argument
     */
    String value() default "";

    String delimiter() default " ";

}
