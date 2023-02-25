package dev.rollczi.litecommands.modern.annotation.route;

import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationResolver;
import dev.rollczi.litecommands.modern.command.editor.CommandEditorContext;

import java.util.Arrays;

public class RouteAnnotationResolver<SENDER> implements CommandAnnotationResolver<SENDER, Route> {

    @Override
    public CommandEditorContext<SENDER> resolve(Object instance, Route annotation, CommandEditorContext<SENDER> context) {
        return context
            .routeName(annotation.name())
            .routeAliases(Arrays.asList(annotation.aliases()));
    }

}
