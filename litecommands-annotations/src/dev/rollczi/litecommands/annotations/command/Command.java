package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.annotations.execute.Execute;

import dev.rollczi.litecommands.strict.StrictMode;
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

    /**
     * Disables/Enables the strict mode.
     * If the strict mode is disabled, the method will be executed even if there are too many arguments.
     */
    StrictMode strict() default StrictMode.DEFAULT;

}
