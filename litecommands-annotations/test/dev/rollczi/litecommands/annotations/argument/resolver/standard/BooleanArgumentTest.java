package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;

public class BooleanArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        Boolean test(@Arg Boolean bool) {
            return bool;
        }
    }

    @Test
    void test() {
        platform.execute("test true")
            .assertSuccess(Boolean.TRUE);
    }

    @Test
    void testInvalid() {
        platform.execute("test dupa")
            .assertFailure();
    }

    @Test
    void testReturn() {
        platform.execute("test false")
            .assertSuccess(Boolean.FALSE);
    }

    @Test
    void testBinaryTrue() {
        platform.execute("test 1")
            .assertSuccess(Boolean.TRUE);
    }

    @Test
    void testBinaryFalse() {
        platform.execute("test 0")
            .assertSuccess(Boolean.FALSE);
    }


}
