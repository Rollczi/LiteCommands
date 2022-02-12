package dev.rollczi.litecommands.annotations;

import org.panda_lang.utilities.inject.annotations.Injectable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Injectable
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Joiner {

}
