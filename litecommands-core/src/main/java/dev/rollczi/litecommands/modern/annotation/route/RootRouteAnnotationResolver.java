package dev.rollczi.litecommands.modern.annotation.route;

import dev.rollczi.litecommands.modern.annotation.processor.CommandAnnotationResolver;
import dev.rollczi.litecommands.modern.editor.CommandEditorContext;

import java.util.Arrays;

public class RootRouteAnnotationResolver<SENDER> implements CommandAnnotationResolver<SENDER, RootRoute> {

    @Override
    public CommandEditorContext<SENDER> resolve(Object instance, RootRoute annotation, CommandEditorContext<SENDER> context) {
        return CommandEditorContext.createRoot();
    }

}
