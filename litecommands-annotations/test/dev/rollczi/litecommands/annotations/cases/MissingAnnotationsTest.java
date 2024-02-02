package dev.rollczi.litecommands.annotations.cases;

import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectInvocationException;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MissingAnnotationsTest {

    @Command(name = "command")
    static class TestCommand {
        @Execute
        void execute(String argument) {
        }
    }

    @Command(name = "command")
    static class TestCommandWithMethod {
        @Execute
        void execute(@Arg String argument) {
            executeAfter(1);
        }

        private void executeAfter(int test) {
        }
    }


    @Test
    @DisplayName("Should throw exception when missing @Arg, @Context or @Bind annotation")
    void test() {
        assertThrows(LiteCommandsReflectInvocationException.class, () -> {
            LiteCommandsFactory.builder(TestSender.class, new TestPlatform())
                .commands(new TestCommand())
                .build();
        });
    }

    @Test
    @DisplayName("Should not throw exception when missing @Arg, @Context or @Bind annotation")
    void test2() {
        LiteCommandsFactory.builder(TestSender.class, new TestPlatform())
            .commands(new TestCommandWithMethod())
            .build();
    }

}
