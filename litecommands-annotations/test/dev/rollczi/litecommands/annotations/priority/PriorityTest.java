package dev.rollczi.litecommands.annotations.priority;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import static dev.rollczi.litecommands.annotations.priority.Priority.*;
import org.junit.jupiter.api.Test;

class PriorityTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        @Priority(Level.NORMAL)
        String executeNormal() {
            return "NORMAL";
        }

        @Execute
        @Priority(Level.HIGHEST)
        String executeHighest() {
            return "HIGHEST";
        }

        @Execute(name = "sub")
        @Priority(Level.LOW)
        String executeLow() {
            return "LOW";
        }

        @Execute(name = "sub")
        @Priority(Level.LOWEST)
        String executeLowest() {
            return "LOWEST";
        }

    }

    @Test
    void test() {
        platform.execute("test")
            .assertSuccess("HIGHEST");
    }

    @Test
    void testSub() {
        platform.execute("test sub")
            .assertSuccess("LOW");
    }

}
