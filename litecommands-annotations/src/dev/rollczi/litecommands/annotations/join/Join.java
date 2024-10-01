package dev.rollczi.litecommands.annotations.join;

import dev.rollczi.litecommands.argument.profile.ArgumentProfileKey;
import dev.rollczi.litecommands.join.JoinProfile;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Join {

    ArgumentProfileKey<JoinProfile> KEY = JoinProfile.KEY;

    String value() default "";

    String separator() default JoinProfile.DEFAULT_SEPARATOR;

    int limit() default JoinProfile.DEFAULT_LIMIT;

}
