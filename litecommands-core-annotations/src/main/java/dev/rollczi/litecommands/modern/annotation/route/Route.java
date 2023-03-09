package dev.rollczi.litecommands.modern.annotation.route;

import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationClassResolver;
import dev.rollczi.litecommands.modern.editor.CommandEditorContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

@Target({ ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Route {

    String name();

    String[] aliases() default {};

    class AnnotationResolver<SENDER> implements CommandAnnotationClassResolver<SENDER, Route> {

        @Override
        public CommandEditorContext<SENDER> resolve(Object instance, Route annotation, CommandEditorContext<SENDER> context) {
            return context
                .routeName(annotation.name())
                .routeAliases(Arrays.asList(annotation.aliases()));
        }

    }
}
