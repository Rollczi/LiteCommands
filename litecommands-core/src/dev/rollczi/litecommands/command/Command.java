package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.command.executor.Execute;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Is used to mark the class as a command.
 * This annotation is required for the class to be registered.
 *
 * @see Execute
 * @see RootCommand
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    /**
     * Represents the part of the command.
     */
    String name();

    /**
     * Represents the aliases of the command.
     */
    String[] aliases() default {};

    class Mock implements Command {

        private final String name;
        private final String[] aliases;

        public Mock(String name, String[] aliases) {
            this.name = name;
            this.aliases = aliases;
        }

        @Override
        public String name() {
            return this.name;
        }

        @Override
        public String[] aliases() {
            return this.aliases;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Command.class;
        }

    }
}
