package dev.rollczi.litecommands.flag;

import dev.rollczi.litecommands.argument.ArgumentKey;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Flag {

    String value();

    ArgumentKey ARGUMENT_KEY = ArgumentKey.typed(FlagArgument.class);

    class Mock implements Flag {

        private final String value;

        public Mock(String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Flag.class;
        }
    }

}
