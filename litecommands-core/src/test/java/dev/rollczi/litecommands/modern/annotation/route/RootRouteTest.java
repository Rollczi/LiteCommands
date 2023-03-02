package dev.rollczi.litecommands.modern.annotation.route;

import dev.rollczi.litecommands.implementation.LiteFactory;
import dev.rollczi.litecommands.modern.LiteCommandsFactory;
import dev.rollczi.litecommands.modern.annotation.execute.Execute;
import dev.rollczi.litecommands.modern.editor.CommandEditorContext;
import dev.rollczi.litecommands.modern.env.FakePlatform;
import dev.rollczi.litecommands.modern.env.FakeSender;
import dev.rollczi.litecommands.modern.invocation.InvocationResult;
import dev.rollczi.litecommands.test.TestPlatform;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RootRouteTest {

    RootRouteAnnotationResolver<?> resolver = new RootRouteAnnotationResolver<>();

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

        InvocationResult<FakeSender> first = platform.execute("first");
        assertTrue(first.isInvoked());

        InvocationResult<FakeSender> second = platform.execute("second");
        assertTrue(second.isInvoked());
    }

}