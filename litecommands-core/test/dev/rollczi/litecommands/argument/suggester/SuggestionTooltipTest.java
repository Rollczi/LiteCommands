package dev.rollczi.litecommands.argument.suggester;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import org.junit.jupiter.api.Test;

public class SuggestionTooltipTest extends LiteTestSpec {

    static LiteTestConfig config = builder -> builder
        .argument(UserData.class, new UserDataResolver());

    static class UserData {
    }

    static class UserDataResolver extends ArgumentResolver<TestSender, UserData> {
        @Override
        protected ParseResult<UserData> parse(Invocation<TestSender> invocation, Argument<UserData> context, String argument) {
            return ParseResult.success(new UserData());
        }

        @Override
        public SuggestionResult suggest(Invocation<TestSender> invocation, Argument<UserData> argument, SuggestionContext context) {
            return SuggestionResult.from(Suggestion.of("1", "user1"));
        }
    }

    @Command(name = "test")
    static class TestCommand {
        @Execute
        void execute(@Arg UserData userData) {

        }
    }

    @Test
    void testTooltip() {
        platform.suggest("test ")
            .assertSuggest(SuggestionResult.from(Suggestion.of("1", "user1")));
    }
}
