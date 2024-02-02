package dev.rollczi.litecommands.annotations.argument;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@ApiStatus.Experimental
public @interface Key {

    @ApiStatus.Experimental
    String value();

}
