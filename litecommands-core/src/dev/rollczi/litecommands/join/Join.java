package dev.rollczi.litecommands.join;

import dev.rollczi.litecommands.argument.ArgumentKey;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Join {

    String separator() default " ";

    int limit() default Integer.MAX_VALUE;

    ArgumentKey ARGUMENT_KEY = ArgumentKey.typed(JoinArgument.class);

    class Mock implements Join {
        private final String separator;
        private final int limit;

        public Mock(String separator, int limit) {
            this.separator = separator;
            this.limit = limit;
        }

        @Override
        public String separator() {
            return separator;
        }

        @Override
        public int limit() {
            return limit;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Join.class;
        }
    }
}
