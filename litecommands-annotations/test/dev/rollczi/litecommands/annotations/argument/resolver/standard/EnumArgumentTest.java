package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;

import java.time.Duration;

class EnumArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        TestEnum test(@Arg TestEnum testEnum) {
            return testEnum;
        }
    }

    enum TestEnum {
        FIRST,
        SECOND,
    }

    @Test
    void test() {
        platform.execute("test FIRST")
            .assertSuccess(TestEnum.FIRST);
    }

    @Test
    void testInvalid() {
        platform.execute("test fodfds")
            .assertFailure();
    }
}