package dev.rollczi.litecommands.modern.annotation.route;

import dev.rollczi.litecommands.modern.LiteCommandsFactory;
import dev.rollczi.litecommands.modern.annotation.execute.Execute;
import dev.rollczi.litecommands.modern.test.env.FakePlatform;
import dev.rollczi.litecommands.modern.test.env.FakeSender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RootRouteTest {

    RootRoute.AnnotationResolver<?> resolver = new RootRoute.AnnotationResolver<>();

    @RootRoute
    static class Command {
        @Execute(name = "first")
        public void test() {}

        @Execute(name = "second")
        public void test2() {}
    }

    @Test
    void testExecuteRootRouteCommands() {
        FakePlatform platform = new FakePlatform();

        LiteCommandsFactory.annotation(FakeSender.class)
            .platform(platform)
            .command(Command.class)
            .register();

        platform.execute("first")
            .assertSuccessful();

        platform.execute("second")
            .assertSuccessful();
    }

}