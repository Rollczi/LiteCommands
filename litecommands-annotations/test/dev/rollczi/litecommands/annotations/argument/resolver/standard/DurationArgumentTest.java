package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import org.junit.jupiter.api.Test;

import java.time.Duration;

class DurationArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        void test(@Arg Duration duration) {
        }

    }

    @Test
    void test() {
        platform.execute("test 1d")
            .assertSuccess();
    }

}