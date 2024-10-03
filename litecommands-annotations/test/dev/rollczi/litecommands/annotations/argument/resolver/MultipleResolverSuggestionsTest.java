package dev.rollczi.litecommands.annotations.argument.resolver;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.MultipleArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.NumberArgumentResolver;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.unit.TestSender;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class MultipleResolverSuggestionsTest extends LiteTestSpec {

    static TestResolver<TestSender> resolver = new TestResolver<>();

    static class TestResolver<SENDER> implements MultipleArgumentResolver<SENDER, String> {

        private Suggestion lastSuggestion;

        @Override
        public ParseResult<String> parse(Invocation<SENDER> invocation, Argument<String> argument, RawInput rawInput) {
            return ParseResult.success(rawInput.next() + " " + rawInput.next());
        }

        @Override
        public Range getRange(Argument<String> stringArgument) {
            return Range.range(1, 2);
        }

        @Override
        public SuggestionResult suggest(Invocation<SENDER> invocation, Argument<String> argument, SuggestionContext context) {
            lastSuggestion = context.getCurrent();
            return SuggestionResult.empty();
        }

    }

    static LiteTestConfig config = builder -> builder
        .argument(String.class, resolver);

    @Command(name = "test")
    static class TestCommand {

        @Execute
        void test(@Arg String one, @Arg String two) {
        }

        @Execute(name = "int")
        void test(@Arg int one, @Arg String two) {
        }

        @Execute(name = "int-reverse")
        void test(@Arg String two, @Arg int one) {
        }


    }

    @Test
    @Order(0)
    void testOneArgument() {
        platform.suggest("test first second");
        assertThat(resolver.lastSuggestion.multilevel()).isEqualTo("first second");
    }

    @Test
    @Order(1)
    void testTwoArguments() {
        platform.suggest("test first second thrid fourth");
        assertThat(resolver.lastSuggestion.multilevel()).isEqualTo("thrid fourth");
    }

    @Test
    @Order(2)
    void testMixedArguments() {
        platform.suggest("test int 0 int-first int-second");
        assertThat(resolver.lastSuggestion.multilevel()).isEqualTo("int-first int-second");

        platform.suggest("test int-reverse first second ")
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), "");
    }

}
