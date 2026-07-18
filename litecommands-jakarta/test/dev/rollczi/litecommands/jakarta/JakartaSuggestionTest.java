package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.junit.jupiter.api.Test;

class JakartaSuggestionTest {

    @Command(name = "test")
    static class TestCommand {
        @Execute(name = "min")
        public void min(@Min(1) @Arg int number) {}

        @Execute(name = "max")
        public void max(@Max(10) @Arg int number) {}
    }

    @Command(name = "unrelated")
    static class UnrelatedTestCommand {
        @Execute(name = "test")
        public void test(@Min(5) @Arg int other, @Min(5) @Max(10) @Arg int target) {}
    }

    TestPlatform platform = LiteCommandsTestFactory.startPlatform(config -> config
        .extension(new LiteJakartaExtension<>())
        .commands(new TestCommand(), new UnrelatedTestCommand())
    );

    @Test
    void testMinSuggestion() {
        platform.suggest("test min ")
            .assertNotEmpty()
            .assertNotSuggest("0");
    }

    @Test
    void testMaxSuggestion() {
        platform.suggest("test max ")
            .assertNotEmpty()
            .assertNotSuggest("11", "50", "100", "500");
    }

    @Test
    void testTargetSuggestionWithUnrelatedMinConstraint() {
        platform.suggest("unrelated test 0 ")
            .assertSuggestAndFlush("5", "6", "7", "8", "9", "10")
            .assertNotSuggest("4", "11");
    }

}
