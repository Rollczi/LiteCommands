package dev.rollczi.litecommands.annotations.intellij;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to define the format of the argument that will be displayed in the LiteCommands IntelliJ plugin.
 */
@Target({ ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.SOURCE)
public @interface IntelliJArgumentInput {

    int priority() default Priority.NORMAL;

    ModificationType modification() default ModificationType.OVERRIDE;

    String format();

    enum ModificationType {
        OVERRIDE,
        APPEND,
    }

    final class Priority {

        private Priority() {}

        public static final int LOWEST = -1000;
        public static final int LOW = -100;
        public static final int NORMAL = 0;
        public static final int HIGH = 100;
        public static final int HIGHEST = 1000;

    }

}
