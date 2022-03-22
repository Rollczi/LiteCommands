package dev.rollczi.litecommands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Section {

    String route();

    String[] aliases() default {};

    int priority() default -1;

    boolean autoPriority() default false;

    int required() default -1;

}
