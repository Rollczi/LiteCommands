package dev.rollczi.litecommands.annotations.command;

import dev.rollczi.litecommands.strict.StrictMode;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Is used to mark the class as a root command (without name).
 * This annotation is required for the class to be registered.
 * If you want to create a command with base name, use {@link Command} annotation.
 * @see Command
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RootCommand {

    /**
     * Disables/Enables the strict mode.
     * If the strict mode is disabled, the method will be executed even if there are too many arguments.
     */
    StrictMode strict() default StrictMode.DEFAULT;

}
