package dev.rollczi.litecommands.annotations.route;

import dev.rollczi.litecommands.annotations.route.RootRoute;
import dev.rollczi.litecommands.editor.CommandEditorContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RootArgAnnotationResolverTest {

    RootRoute.AnnotationResolver<?> resolver = new RootRoute.AnnotationResolver<>();

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