package dev.rollczi.litecommands.shared;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TemporalAmountParserTest {

    @CsvSource({
        "1m20s,80",
        "1m,60",
        "1h,3600",
        "1d,86400",
        "1d10m,87000",
        "1d10m20s,87020",
        "1d10m20s30ms,87020.03",
        "1d10m20s30ms40us,87020.03004",
        "50ns,0.00000005",
        "3w,1814400",
        "1mo,2592000",
        "1y,31536000",
    })
    @ParameterizedTest
    void testParse(String input, double expected) {
        TemporalAmountParser<Duration> temporalAmountParser = TemporalAmountParser.DATE_TIME_UNITS;

        Duration duration = temporalAmountParser.parse(input);

        assertEquals(expected, duration.toNanos() / 1_000_000_000.0);
    }

    @Test
    void testThrowsIllegalArgumentException() {
        TemporalAmountParser<Duration> temporalAmountParser = TemporalAmountParser.createDuration()
            .withUnit("s", ChronoUnit.SECONDS);

        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.parse("1d10m"));
    }

    @CsvSource({
        "60,1m",
        "3600,1h",
        "86400,1d",
        "87000,1d10m",
        "87020,1d10m20s",
        "1814400,3w",
        "2592000,1mo",
        "31536000,1y",
    })
    @ParameterizedTest
    void testFormat(int seconds, String expected) {
        TemporalAmountParser<Duration> temporalAmountParser = TemporalAmountParser.DATE_TIME_UNITS;

        Duration duration = Duration.ofSeconds(seconds);
        String formatted = temporalAmountParser.format(duration);

        assertEquals(expected, formatted);
    }

}
