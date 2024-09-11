package dev.rollczi.litecommands.annotations.execute;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.Test;

class ExecuteDefaultTest extends LiteTestSpec {

    @Command(name = "help")
    static class TestCommand {

        @ExecuteDefault
        String execute(@Context TestSender sender) {
            return "#help";
        }

        @Execute(name = "vip")
        String vip(@Context TestSender sender) {
            return "#help:vip";
        }

        @Execute
        String withArg(@Context TestSender sender, @Arg int page) {
            return "#help:" + page;
        }

    }

    @Test
    void testDefault() {
        platform.execute("help")
            .assertSuccess("#help");

        platform.execute("help args")
            .assertSuccess("#help");

        platform.execute("help other args")
            .assertSuccess("#help");

        platform.execute("help 1 args")
            .assertSuccess("#help");
    }

    @Test
    void testDefaultWithSubCommand() {
        platform.execute("help vip")
            .assertSuccess("#help:vip");
    }

    @Test
    void testDefaultWithArg() {
        platform.execute("help 1")
            .assertSuccess("#help:1");

        platform.execute("help 2")
            .assertSuccess("#help:2");
    }

}
