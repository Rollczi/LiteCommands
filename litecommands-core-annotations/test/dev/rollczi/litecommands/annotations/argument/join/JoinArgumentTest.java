package dev.rollczi.litecommands.annotations.argument.join;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.join.Join;
import dev.rollczi.litecommands.command.Command;
import dev.rollczi.litecommands.execute.Execute;
import org.junit.jupiter.api.Test;

class JoinArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        public String test(@Join String args) {
            return args;
        }

        @Execute(name = "comma")
        public String testComma(@Join(separator = ",") String args) {
            return args;
        }

    }

    @Test
    void test() {
        platform.execute("test a b c")
            .assertSuccess("a b c");
    }

    @Test
    void testComma() {
        platform.execute("test comma a b c")
            .assertSuccess("a,b,c");
    }

}