package dev.rollczi.litecommands.argument.basictype.time;

import dev.rollczi.litecommands.test.TestUtils;
import dev.rollczi.litecommands.suggestion.Suggestion;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import panda.std.Result;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TemporalAccessorArgumentTest {

    private static final String PREFIX_PACKAGE = "dev.rollczi.litecommands.argument.basictype.time.";

    @CsvSource({
        "InstantArgument, 2021-01-01 00:00:00",

        "OffsetDateTimeArgument, 2021-01-01 00:00:00 +01:00",
        "ZonedDateTimeArgument, 2021-01-01 00:00:00 UTC",
        "OffsetTimeArgument, 00:00:00 +01:00",

        "LocalDateTimeArgument, 2021-01-01 00:00:00",
        "LocalDateArgument, 2021-01-01",
        "LocalTimeArgument, 00:00:00",

        "YearMonthArgument, 2021-01",
        "YearArgument, 2021",
        "MonthDayArgument, 12-01",
        "MonthArgument, 12",
        "DayOfWeekArgument, Mon",

        "ZoneOffsetArgument, +01:00",

        "JapaneseDateArgument, Reiwa 0001-01-01",
        "HijrahDateArgument, AH 1442-01-01",
        "MinguoDateArgument, R.O.C. 0110-01-01",
        "ThaiBuddhistDateArgument, BE 2564-01-01",
    })
    @ParameterizedTest
    void testParseAndSuggest(
        String className,
        String arguments
    ) {
        TemporalAccessorArgument<?> temporalAccessorArgument = this.createTemporalAccessorArgument(className);

        {
            Result<?, ?> result = temporalAccessorArgument.parseMultilevel(TestUtils.invocation(), arguments);

            assertTrue(result.isOk());
        }

        {
            List<Suggestion> actual = temporalAccessorArgument.suggest(TestUtils.invocation());

            for (Suggestion suggestion : actual) {
                Result<?, ?> result = temporalAccessorArgument.parseMultilevel(TestUtils.invocation(), suggestion.multilevel());

                assertTrue(result.isOk());
            }
        }
    }

    private TemporalAccessorArgument<?> createTemporalAccessorArgument(String className) {
        try {
            return (TemporalAccessorArgument<?>) Class.forName(PREFIX_PACKAGE + className).newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

}
