package dev.rollczi.litecommands.annotations.priority;

import dev.rollczi.litecommands.priority.PriorityLevel;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Priority {

    Level value() default Level.NORMAL;

    int custom() default 0;

    enum Level {

        LOWEST(PriorityLevel.LOWEST),
        LOW(PriorityLevel.LOW),
        NORMAL(PriorityLevel.NORMAL),
        HIGH(PriorityLevel.HIGH),
        HIGHEST(PriorityLevel.HIGHEST);

        private final PriorityLevel substitute;

        Level(PriorityLevel substitute) {
            this.substitute = substitute;
        }

        public PriorityLevel toPriorityLevel() {
            return substitute;
        }

    }

}
