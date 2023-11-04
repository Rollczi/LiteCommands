package dev.rollczi.litecommands.annotations.permission;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiStatus.Internal
public @interface Permissions {

    @ApiStatus.Internal
    Permission[] value();

}

