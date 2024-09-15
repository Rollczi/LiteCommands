package dev.rollczi.litecommands.annotations.execute;

import dev.rollczi.litecommands.strict.StrictMode;
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

    /**
     * Disables/Enables the strict mode.
     * If the strict mode is disabled, the method will be executed even if there are too many arguments.
     * Default is {@link StrictMode#DISABLED}
     */
    StrictMode strict() default StrictMode.DISABLED;

}
