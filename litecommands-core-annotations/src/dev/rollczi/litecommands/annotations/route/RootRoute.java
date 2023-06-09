package dev.rollczi.litecommands.annotations.route;

import dev.rollczi.litecommands.annotations.processor.CommandAnnotationClassResolver;
import dev.rollczi.litecommands.command.builder.CommandBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RootRoute {

    class AnnotationResolver<SENDER> implements CommandAnnotationClassResolver<SENDER, RootRoute> {

        @Override
        public CommandBuilder<SENDER> resolve(Object instance, RootRoute annotation, CommandBuilder<SENDER> context) {
            return CommandBuilder.createRoot();
        }

    }

}
