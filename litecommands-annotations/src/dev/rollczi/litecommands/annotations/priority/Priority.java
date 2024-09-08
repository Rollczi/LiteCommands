package dev.rollczi.litecommands.annotations.priority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Priority {

    @MagicConstant(valuesFromClass = PriorityValue.class)
    int value() default PriorityValue.NORMAL;

}
