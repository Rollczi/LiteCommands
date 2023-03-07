package dev.rollczi.litecommands.modern.annotation.argument;

import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.test.env.FakeSender;
import dev.rollczi.litecommands.modern.test.env.TestUtil;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.range.Range;
import dev.rollczi.litecommands.modern.suggestion.SuggestionContext;
import dev.rollczi.litecommands.modern.suggestion.SuggestionResult;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class OneAnnotationArgumentTest {

    static class TestOneAnnotationArgument extends OneAnnotationArgument<FakeSender, String> {

        @Override
        protected ArgumentResult<String> parse(Invocation<FakeSender> invocation, String argument, ParameterArgument<Arg, String> context) {
            return ArgumentResult.success(() -> argument);
        }

        @Override
        public SuggestionResult suggest(Invocation<FakeSender> invocation, ParameterArgument<Arg, String> argument, SuggestionContext suggestion) {
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
        String suggestion = result.getSuggestions().get(0).head();

        assertEquals("text", suggestion);
    }

}