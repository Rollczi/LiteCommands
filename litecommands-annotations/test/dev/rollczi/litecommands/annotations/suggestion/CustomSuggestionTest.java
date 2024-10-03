package dev.rollczi.litecommands.annotations.suggestion;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CustomSuggestionTest extends LiteTestSpec {

    static LiteTestConfig config = builder -> builder
        .argumentSuggestion(String.class, SuggestionResult.of("default-suggestion"))
        .argumentSuggestion(String.class, ArgumentKey.of("custom"), SuggestionResult.of("custom-suggestion"));

    @Command(name = "test")
    static class TestCommand {
        @Execute
        void execute(@Arg String normal, @Arg("custom") String custom) {}
    }

    @Test
    @DisplayName("should suggest default suggestion")
    void testDefault() {
        platform.suggest("test ")
            .assertSuggest("default-suggestion");
    }

    @Test
    @DisplayName("should suggest default suggestion with ignore case")
    void testIgnoreCase() {
        platform.suggest("test D")
            .assertSuggest("default-suggestion");

        platform.suggest("test DEFAULT-SUGGESTION")
            .assertSuggest("default-suggestion");
    }

    @Test
    @DisplayName("should suggest custom suggestion")
    void testCustom() {
        platform.suggest("test text ")
            .assertSuggest("custom-suggestion");
    }

}
