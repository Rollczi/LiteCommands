package dev.rollczi.litecommands.annotations.execute;

import dev.rollczi.litecommands.annotations.command.Command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents the method that will be executed when the command is called.
 * This annotation is required for the method to be executed.
 *
 * @see Command
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Execute {

    /**
     * Represents the name of the command.
     */
    String name() default "";

    /**
     * Represents the aliases of the command.
     */
    String[] aliases() default {};

}
