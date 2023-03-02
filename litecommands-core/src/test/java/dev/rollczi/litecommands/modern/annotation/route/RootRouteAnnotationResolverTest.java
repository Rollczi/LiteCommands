package dev.rollczi.litecommands.modern.annotation.route;

import dev.rollczi.litecommands.modern.editor.CommandEditorContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RootRouteAnnotationResolverTest {

    RootRouteAnnotationResolver<?> resolver = new RootRouteAnnotationResolver<>();

    @RootRoute
    static class Command {}

    @Test
    void test() {
        Command command = new Command();
        RootRoute rootRoute = Command.class.getAnnotation(RootRoute.class);

        CommandEditorContext<?> context = resolver.resolve(command, rootRoute, CommandEditorContext.create());

        assertEquals("", context.name());
    }

}