package dev.rollczi.litecommands.annotations.flag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Flag {

    String value();
    
}
