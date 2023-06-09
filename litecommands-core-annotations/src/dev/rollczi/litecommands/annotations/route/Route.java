package dev.rollczi.litecommands.annotations.route;

import dev.rollczi.litecommands.annotations.processor.CommandAnnotationClassResolver;
import dev.rollczi.litecommands.command.builder.CommandBuilder;
import dev.rollczi.litecommands.util.LiteCommandsUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Route {

    String name();

    String[] aliases() default {};

    class AnnotationResolver<SENDER> implements CommandAnnotationClassResolver<SENDER, Route> {

        @Override
        public CommandBuilder<SENDER> resolve(Object instance, Route annotation, CommandBuilder<SENDER> context) {
            boolean isNotEmpty = LiteCommandsUtil.checkConsistent(annotation.name(), annotation.aliases());

            if (isNotEmpty) {
                return context
                    .routeName(annotation.name())
                    .routeAliases(Arrays.asList(annotation.aliases()));
            }

            throw new IllegalArgumentException("Route name cannot be empty");
        }

    }

}
