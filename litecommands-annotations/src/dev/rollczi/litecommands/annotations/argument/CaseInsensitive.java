package dev.rollczi.litecommands.annotations.argument;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an enum parameter's value should be treated in a case-insensitive manner.
 * <p>
 * This annotation is experimental and may be subject to change.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ApiStatus.Experimental
public @interface CaseInsensitive {
}
