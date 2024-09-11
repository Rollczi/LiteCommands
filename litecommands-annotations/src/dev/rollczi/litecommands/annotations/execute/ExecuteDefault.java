package dev.rollczi.litecommands.annotations.execute;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.ApiStatus;

/**
 * Represents the method that will be executed when the command is called and no other method is found.
 * This will ignore all input arguments.
 *
 * @see Execute
 */
@ApiStatus.Experimental
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExecuteDefault {
}
