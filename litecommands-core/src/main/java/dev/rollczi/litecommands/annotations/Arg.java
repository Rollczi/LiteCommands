package dev.rollczi.litecommands.annotations;

import dev.rollczi.litecommands.inject.ArgumentHandler;
import org.panda_lang.utilities.inject.annotations.Injectable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Injectable
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Arg {

    int value();

    //TODO: Add option to use only one handler.
    // public void execute1(@Arg(0) String custom) { ... }
    // public void execute2(@Arg(value = 0, only = Handler1.class) String custom) { ... }
    // public void execute3(@Arg(value = 0, only = Handler2.class) String custom) { ... }
    Class<? extends ArgumentHandler> only() default ArgumentHandler.class;

}
