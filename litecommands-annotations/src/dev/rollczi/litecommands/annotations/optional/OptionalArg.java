package dev.rollczi.litecommands.annotations.optional;

import dev.rollczi.litecommands.annotations.intellij.IntelliJArgumentInput;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IntelliJArgumentInput(format = "[%s]")
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalArg {

    String value() default "";

}
