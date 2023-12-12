package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LiteJakartaExtensionTest {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        public void execute(@Size(min = 5, max = 10) @Arg String text) {}

    }

    TestPlatform platform = LiteCommandsTestFactory.startPlatform(config -> config
        .commands(
            new TestCommand()
        )
        .extension(new LiteJakartaExtension<>())
    );

    @Test
    void testValid() {
        platform.execute("test long-text")
            .assertSuccess();
    }

    @Test
    void testInvalid() {
        JakartaResult testText = platform.execute("test text")
            .assertFailedAs(JakartaResult.class);

        assertThat(testText.getViolations()).hasSize(1);
    }

}
