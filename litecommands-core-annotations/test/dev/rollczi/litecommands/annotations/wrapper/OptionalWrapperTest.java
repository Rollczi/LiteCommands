package dev.rollczi.litecommands.annotations.wrapper;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.arg.Arg;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.command.Command;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class OptionalWrapperTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {
        @Execute
        public void test(@Arg Optional<Long> arg) {}
    }

    @Test
    void testWithoutArguments() {
        platform.execute("test")
            .assertSuccess();
    }

    @Test
    void testWithArgument() {
        platform.execute("test 123")
            .assertSuccess();
    }

    @Test
    void testWithTooManyArguments() {
        platform.execute("test 123 456")
            .assertFailure();
    }

    @Test
    void testWithInvalidArgument() {
        platform.execute("test abc")
            .assertFailure();
    }

}
