package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import org.junit.jupiter.api.Test;

class CustomExceptionTest {

    TestPlatform platform = TestFactory.create(builder -> builder
        .command(Command.class)
        .resultHandler(CustomException.class, (testHandle, invocation, value) -> testHandle.message("message"))
    );

    static class CustomException extends RuntimeException {

        public CustomException(String message) {
            super(message);
        }

    }

    @Route(name = "test")
    static class Command {
        @Execute
        void handle() {
            throw new CustomException("result");
        }
    }

    @Test
    void test() {
        platform.execute("test")
            .assertSuccess()
            .assertMessage("message")
            .assertResultIs(CustomException.class);
    }


}
