package dev.rollczi.litecommands.argument;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Arg {

    String value() default "";

    class Mock implements Arg {

        private final String value;

        public Mock(String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public Class<? extends Arg> annotationType() {
            return Arg.class;
        }

    }

}
