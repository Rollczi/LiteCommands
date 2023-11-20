package dev.rollczi.litecommands.annotations.cases;

import dev.rollczi.litecommands.LiteCommandsFactory;
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

    @Test
    @DisplayName("Should throw exception when missing @Arg, @Context or @Bind annotation")
    void test() {
        assertThrows(LiteCommandsReflectInvocationException.class, () -> {
            LiteCommandsFactory.builder(TestSender.class, new TestPlatform())
                .commands(new TestCommand())
                .build();
        });
    }

}
