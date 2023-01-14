package dev.rollczi.litecommands.command.async;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

class AsyncAnnotationTest {

    TestPlatform platform = TestFactory.withCommandsUniversalHandler(Command.class);

    @Route(name = "command")
    static class Command {

        @Async
        @Execute(route = "async")
        String execute() {
            return "async";
        }

    }

    @Test
    void test() {
        this.platform.execute("command", "async")
            .assertSuccess()
            .assertResultIs(CompletableFuture.class);
    }

}
