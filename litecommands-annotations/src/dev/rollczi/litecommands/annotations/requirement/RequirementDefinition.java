package dev.rollczi.litecommands.annotations.requirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirementDefinition {

    /**
     * Type of the requirement.
     */
    Type type();

    /**
     * Name providers are used to get the name of the requirement.
     * If the name is not provided (or empty) the name will be the same as the parameter name.
     */
    String[] nameProviders() default {};

    enum Type {
        /**
         * Argument resolver
         */
        ARGUMENT,
        /**
         * Context provider
         */
        CONTEXT,
        /**
         * Bind provider
         */
        BIND,
    }

}
