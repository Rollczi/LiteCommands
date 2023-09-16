package dev.rollczi.litecommands.annotations.exception;

import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.command.Command;
import dev.rollczi.litecommands.command.executor.Execute;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ExceptionHandleTest extends LiteTestSpec {

    private static final Set<String> exceptions = new HashSet<>();

    static LiteConfig config = builder -> builder
            .exception(IllegalArgumentException.class, (invocation, exception) -> exceptions.add("IllegalArgumentException"))
            .exception(RuntimeException.class, (invocation, exception) -> exceptions.add("RuntimeException"));

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