package dev.rollczi.litecommands.argument.basictype.time;

import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.test.TestUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import panda.std.Result;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TemporalAmountArgumentTest {

    private static final String PREFIX_PACKAGE = "dev.rollczi.litecommands.argument.basictype.time.";

    @CsvSource({
        "PeriodArgument, 1y2m3d",
        "DurationArgument, 1h2m3s",
    })
    @ParameterizedTest
    void testParseAndSuggest(String className, String pattern) {
        TemporalAmountArgument<?> temporalAmountArgument = this.createTemporalAmountArgument(className);

        {
            Result<?, ?> result = temporalAmountArgument.parseMultilevel(TestUtils.invocation(), pattern);

            assertTrue(result.isOk());
        }

        {
            List<Suggestion> actual = temporalAmountArgument.suggest(TestUtils.invocation());

            for (Suggestion suggestion : actual) {
                Result<?, ?> result = temporalAmountArgument.parseMultilevel(TestUtils.invocation(), suggestion.multilevel());

                assertTrue(result.isOk());
            }
        }
    }

    private TemporalAmountArgument<?> createTemporalAmountArgument(String className) {
        try {
            return (TemporalAmountArgument<?>) Class.forName(PREFIX_PACKAGE + className).newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

}
