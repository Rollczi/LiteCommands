package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.argument.suggestion.SuggestionContext;
import dev.rollczi.litecommands.argument.suggestion.SuggestionResult;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.unit.TestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OneAnnotationArgumentTest {

    static class TestOneAnnotationArgument extends OneAnnotationArgument<TestSender, String, ArgParameterArgument<String>> {

        @Override
        public ParseResult<String> parseTyped(Invocation<TestSender> invocation, ArgParameterArgument<String> argument, RawInput rawInput) {
            return ParseResult.success(() -> rawInput.next());
        }

        @Override
        public SuggestionResult suggestTyped(Invocation<TestSender> invocation, ArgParameterArgument<String> argument, SuggestionContext context) {
            return SuggestionResult.of("text");
        }

        @Override
        public Class<? extends Argument> getArgumentType() {
            return ArgParameterArgument.class;
        }

    }

    private final TestOneAnnotationArgument argument = new TestOneAnnotationArgument();

    @Test
    void testRange() {
        Range range = argument.getRange();

        assertEquals(Range.ONE, range);
        assertFalse(range.isInRange(0));
        assertTrue(range.isInRange(1));
        assertFalse(range.isInRange(2));
    }

    @Test
    void testParse() {
        ParseResult<String> parseResult = argument.parse(TestUtil.invocation("test"), null, RawInput.of("test"));
        String value = parseResult.getSuccessfulResult().get();

        assertEquals("test", value);
    }

    @Test
    void testSuggest() {
        SuggestionResult result = argument.suggest(TestUtil.invocation("test"), null, null);
        String suggestion = result.getSuggestions()
            .stream()
            .findFirst()
            .orElseThrow(() -> new AssertionError("Suggestion not found"))
            .multilevel();

        assertEquals("text", suggestion);
    }

}