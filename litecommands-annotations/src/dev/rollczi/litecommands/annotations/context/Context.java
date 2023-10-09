package dev.rollczi.litecommands.annotations.context;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Context {

    class Mock implements Context {
        @Override
        public Class<? extends Annotation> annotationType() {
            return Context.class;
        }
    }

}
