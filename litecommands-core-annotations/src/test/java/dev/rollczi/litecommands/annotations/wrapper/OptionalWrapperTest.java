package dev.rollczi.litecommands.annotations.wrapper;

import dev.rollczi.litecommands.annotations.LiteTest;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.route.Route;
import org.junit.jupiter.api.Test;

import java.util.Optional;

@LiteTest
class OptionalWrapperTest extends LiteTestSpec {

    @Route(name = "test")
    static class TestCommand {
        @Execute
        public void test(@Arg Optional<Long> arg) {}
    }

    @Test
    void testWithoutArguments() {
        platform.execute("test")
            .assertSuccessful();
    }

    @Test
    void testWithArgument() {
        platform.execute("test 123")
            .assertSuccessful();
    }

    @Test
    void testWithTooManyArguments() {
        platform.execute("test 123 456")
            .assertFailed();
    }

    @Test
    void testWithInvalidArgument() {
        platform.execute("test abc")
            .assertFailed();
    }

}
