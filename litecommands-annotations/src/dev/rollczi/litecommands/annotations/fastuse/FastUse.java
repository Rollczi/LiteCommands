package dev.rollczi.litecommands.annotations.fastuse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FastUse {

    /**
     * Represents the name of the command.
     */
    String name();

    /**
     * Represents the aliases of the command.
     */
    String[] aliases() default {};

    /**
     * Represents if the meta of root should be copied to the fast use command.
     */
    boolean copyMeta() default true;

}
