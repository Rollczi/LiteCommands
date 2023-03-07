package dev.rollczi.litecommands.modern.env;

import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.editor.CommandEditorContext;

import java.util.Collection;

public class AssertCommand {

    public static <T>CommandRoute<T> assertSingle(CommandEditorContext<T> context) {
        Collection<CommandRoute<T>> routeCollection = context.build(CommandRoute.createRoot());

        if (routeCollection.size() != 1) {
            throw new IllegalStateException("Expected single route, got " + routeCollection.size());
        }

        return routeCollection.iterator().next();
    }

}
