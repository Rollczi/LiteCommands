package dev.rollczi.litecommands.annotations.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Is used to mark the class as a root command (without name).
 * This annotation is required for the class to be registered.
 * If you want to create a command with base name, use {@link Command} annotation.
 * @see dev.rollczi.litecommands.annotations.command.Command
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RootCommand {

}
