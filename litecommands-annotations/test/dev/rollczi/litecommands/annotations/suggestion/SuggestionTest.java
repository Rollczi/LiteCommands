package dev.rollczi.litecommands.annotations.suggestion;

import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.test.LiteTestGuild;
import dev.rollczi.litecommands.annotations.test.LiteTestUser;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.parser.Parser;
import dev.rollczi.litecommands.argument.suggester.Suggester;
import dev.rollczi.litecommands.argument.suggester.SuggesterChainAccessor;
import dev.rollczi.litecommands.argument.suggester.SuggesterChained;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SuggestionTest extends LiteTestSpec {

    static LiteConfig config = builder -> builder
        .argumentParser(LiteTestUser.class, new DummyParser<>())
        .argumentParser(LiteTestGuild.class, new DummyParser<>())
        .argumentSuggester(LiteTestUser.class, new UserSuggester<>())
        .argumentSuggester(LiteTestGuild.class, new GuildSuggester<>());

    static class DummyParser<S, P> implements Parser<S, P> {

        @Override
        public ParseResult<P> parse(Invocation<S> invocation, Argument<P> argument, RawInput input) {
            input.next();
            return ParseResult.success(null);
        }

        @Override
        public Range getRange(Argument<P> argument) {
            return Range.ONE;
        }

    }

    static class UserSuggester<S> implements Suggester<S, LiteTestUser> {

        @Override
        public SuggestionResult suggest(Invocation<S> invocation, Argument<LiteTestUser> argument, SuggestionContext context) {
            return SuggestionResult.of("user-suggestion");
        }

    }

    static class GuildSuggester<S> implements SuggesterChained<S, LiteTestGuild> {

        @Override
        public SuggestionResult suggest(Invocation<S> invocation, Argument<LiteTestGuild> argument, SuggestionContext context, SuggesterChainAccessor<S> chainAccessor) {
            return chainAccessor.suggest(invocation, Argument.of(argument, LiteTestUser.class), context);
        }

    }

    @Command(name = "command")
    static class TestCommand {

        @Execute(name = "user")
        void execute(@Arg LiteTestUser user) {
        }

        @Execute(name = "guild")
        void execute(@Arg LiteTestGuild guild) {
        }

    }

    @Test
    @DisplayName("Should suggest user for user argument and guild argument")
    void testSuggestion() {
        platform.suggest("command user ")
            .assertSuggest("user-suggestion");

        platform.suggest("command guild ")
            .assertSuggest("user-suggestion");
    }

}
