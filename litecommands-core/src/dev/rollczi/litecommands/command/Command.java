package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.command.executor.Execute;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Is used to mark the class as a command.
 * This annotation is required for the class to be registered.
 *
 * @see Execute
 * @see RootCommand
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    /**
     * Represents the part of the command.
     */
    String name();

    /**
     * Represents the aliases of the command.
     */
    String[] aliases() default {};

}
