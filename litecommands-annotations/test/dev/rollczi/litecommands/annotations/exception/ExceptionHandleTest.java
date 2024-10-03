package dev.rollczi.litecommands.annotations.exception;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ExceptionHandleTest extends LiteTestSpec {

    private static final Set<String> exceptions = new HashSet<>();

    static LiteTestConfig config = builder -> builder
            .exception(IllegalArgumentException.class, (invocation, exception, chain) -> exceptions.add("IllegalArgumentException"))
            .exception(RuntimeException.class, (invocation, exception, chain) -> exceptions.add("RuntimeException"));

    @Command(name = "test")
    static class TestCommand {
        @Execute(name = "illegal")
        void execute() {
            throw new IllegalArgumentException();
        }

        @Execute(name = "state")
        void execute2() {
            throw new IllegalStateException();
        }
    }

    @Test
    @DisplayName("Should handle exceptions")
    void test() {
        platform.execute("test illegal")
            .assertThrows(IllegalArgumentException.class);

        assertTrue(exceptions.contains("IllegalArgumentException"));

        platform.execute("test state")
            .assertThrows(IllegalStateException.class);

        assertTrue(exceptions.contains("RuntimeException"));
    }


}