package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import org.junit.jupiter.api.Test;

public class OptionalArgTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        String test(@OptionalArg String arg) {
            return String.valueOf(arg);
        }

    }

    @Test
    public void test() {
        platform.execute("test")
            .assertSuccess("null");

        platform.execute("test 1")
            .assertSuccess("1");
    }

}
