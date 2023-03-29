package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.OneAnnotationArgument;
import dev.rollczi.litecommands.annotations.argument.ParameterArgument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.unit.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OneAnnotationArgumentTest {

    static class TestOneAnnotationArgument extends OneAnnotationArgument<TestSender, String> {

        @Override
        protected ArgumentResult<String> parse(Invocation<TestSender> invocation, String argument, ParameterArgument<Arg, String> context) {
            return ArgumentResult.success(() -> argument);
        }

        @Override
        public SuggestionResult suggest(Invocation<TestSender> invocation, ParameterArgument<Arg, String> argument, SuggestionContext suggestion) {
            return SuggestionResult.of("text");
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
        ArgumentResult<String> parseResult = argument.parse(TestUtil.invocation("test"), null, Collections.singletonList("test"));
        String value = parseResult.getSuccessfulResult().getExpectedProvider().get();

        assertEquals("test", value);
    }

    @Test
    void testSuggest() {
        SuggestionResult result = argument.suggest(TestUtil.invocation("test"), null, null);
        String suggestion = result.getSuggestions().get(0).multilevel();

        assertEquals("text", suggestion);
    }

}