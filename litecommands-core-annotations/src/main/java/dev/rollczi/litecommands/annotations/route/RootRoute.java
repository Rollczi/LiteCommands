package dev.rollczi.litecommands.annotations.route;

import dev.rollczi.litecommands.annotations.processor.CommandAnnotationClassResolver;
import dev.rollczi.litecommands.editor.CommandEditorContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RootRoute {

    class AnnotationResolver<SENDER> implements CommandAnnotationClassResolver<SENDER, RootRoute> {

        @Override
        public CommandEditorContext<SENDER> resolve(Object instance, RootRoute annotation, CommandEditorContext<SENDER> context) {
            return CommandEditorContext.createRoot();
        }

    }

}
