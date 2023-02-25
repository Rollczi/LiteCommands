package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LiteExceptionTest {

    TestPlatform platform = TestFactory.create(builder -> builder
        .command(Command.class)
        .resultHandler(String.class, (testHandle, invocation, value) -> testHandle.message("message"))
    );

    @Route(name = "test")
    static class Command {

        @Execute
        void handle() {
            throw new LiteException("result");
        }

    }

    @Test
    void test() {
        LiteException test = platform.execute("test")
            .assertSuccess()
            .assertMessage("message")
            .assertResultIs(LiteException.class);

        assertEquals("result", test.getResult());
    }


}
