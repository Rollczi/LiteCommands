package dev.rollczi.litecommands.command.executor;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents the method that will be executed when the command is called.
 * This annotation is required for the method to be executed.
 *
 * @see dev.rollczi.litecommands.command.Command
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Execute {

    /**
     * Represents the name of the command.
     */
    String name() default "";

    /**
     * Represents the aliases of the command.
     */
    String[] aliases() default {};

    class Mock implements Execute {
        private final String name;
        private final String[] aliases;

        public Mock(String name, String[] aliases) {
            this.name = name;
            this.aliases = aliases;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String[] aliases() {
            return aliases;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Execute.class;
        }
    }

}
