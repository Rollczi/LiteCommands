package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.permission.PermissionSection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Permissions.class)
public @interface Permission {

    String[] value();
    PermissionSection.Type type() default PermissionSection.Type.AND;

}

