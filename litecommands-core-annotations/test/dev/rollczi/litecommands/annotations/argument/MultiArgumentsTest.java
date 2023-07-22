package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.arg.Arg;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.argument.resolver.MultipleArgumentResolver;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MultiArgumentsTest extends LiteTestSpec {

    static LiteConfig config = builder -> builder
            .argumentSuggester(String.class, SuggestionResult.of("suggestion"))
            .argument(Position.class, new PositionArg<>());

    static class PositionArg<S> implements MultipleArgumentResolver<S, Position> {

        @Override
        public ParseResult<Position> parse(Invocation<S> invocation, Argument<Position> argument, RawInput rawInput) {
            try {
                Position position = new Position(
                    rawInput.nextInt(),
                    rawInput.nextInt(),
                    rawInput.nextInt()
                );

                return ParseResult.success(position);
            }
            catch (NumberFormatException exception) {
                return ParseResult.failure(InvalidUsage.Cause.INVALID_ARGUMENT);
            }
        }

        @Override
        public SuggestionResult suggest(Invocation<S> invocation, Argument<Position> argument, SuggestionContext context) {
            return SuggestionResult.of("1 2 3", "4 5 6", "7 8 9");
        }

        @Override
        public Range getRange(Argument<Position> positionArgument) {
            return Range.of(3);
        }
    }

    static class Position {
       final int x;
       final int y;
       final int z;

        public Position(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    @Command(name = "command")
    static class TestCommand {
        @Execute
        void execute(@Arg String arg0, @Arg String arg1, @Arg Position position) {}
        @Execute(name = "sub")
        void executeSub(@Arg Position position) {}
    }

    @Test
    @DisplayName("Should pass because of correct arguments count")
    void testCorrect() {
        platform.execute("command arg0 arg1 1 2 3")
            .assertSuccess();

        platform.execute("command sub 1 2 3")
            .assertSuccess();
    }

    @Test
    @DisplayName("Should fail because of incorrect arguments count")
    void testIncorrect() {
        platform.execute("command")
            .assertFailedAs(InvalidUsage.class);

        platform.execute("command arg0")
            .assertFailedAs(InvalidUsage.class);

        platform.execute("command arg0 arg1")
            .assertFailedAs(InvalidUsage.class);

        platform.execute("command arg0 arg1 1")
            .assertFailedAs(InvalidUsage.class);

        platform.execute("command arg0 arg1 1 2")
            .assertFailedAs(InvalidUsage.class);

        platform.execute("command sub")
            .assertFailedAs(InvalidUsage.class);

        platform.execute("command sub 1")
            .assertFailedAs(InvalidUsage.class);

        platform.execute("command sub 1 2")
            .assertFailedAs(InvalidUsage.class);
    }

    @Test
    @DisplayName("Should fail because of incorrect input")
    void testIncorrectInput() {
        platform.execute("command arg0 arg1 1 2 a")
            .assertFailedAs(InvalidUsage.class);

        platform.execute("command sub 1 2 a")
            .assertFailedAs(InvalidUsage.class);
    }

    @Test
    @DisplayName("Should suggest main command")
    void testSuggestMain() {
        platform.suggest("command")
            .assertSuggest("command");
    }

    @Test
    @DisplayName("Should suggest argument and sub command")
    void testSuggestNext() {
        platform.suggest("command ")
            .assertSuggest("sub", "suggestion");

        platform.suggest("command s")
            .assertSuggest("sub", "suggestion");

        platform.suggest("command sug")
            .assertSuggest("suggestion");
    }

    @Test
    @DisplayName("Should suggest multiple arguments")
    void testSuggestMultiple() {
        platform.suggest("command arg0 arg1 ")
            .assertSuggest("1 2 3", "4 5 6", "7 8 9");

        platform.suggest("command arg0 arg1 1 ")
            .assertSuggest("2 3");

        platform.suggest("command arg0 arg1 1 2 ")
            .assertSuggest("3");
    }

}
