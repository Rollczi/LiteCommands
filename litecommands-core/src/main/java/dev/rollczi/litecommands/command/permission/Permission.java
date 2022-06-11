package dev.rollczi.litecommands.command.permission;

import dev.rollczi.litecommands.factory.FactoryAnnotationResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Permissions.class)
public @interface Permission {

    String[] value() default {};

    FactoryAnnotationResolver<Permission> RESOLVER = new PermissionsAnnotationResolver();
    FactoryAnnotationResolver<Permissions> REPEATABLE_RESOLVER = new PermissionAnnotationResolver();

}
