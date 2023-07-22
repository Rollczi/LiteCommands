package dev.rollczi.litecommands.annotations.argument.join;

import dev.rollczi.litecommands.annotations.command.requirement.RequirementAnnotation;
import dev.rollczi.litecommands.argument.ArgumentKey;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@RequirementAnnotation
public @interface Join {

    String separator() default " ";

    int limit() default Integer.MAX_VALUE;

    ArgumentKey ARGUMENT_KEY = ArgumentKey.typed(JoinArgument.class);

}