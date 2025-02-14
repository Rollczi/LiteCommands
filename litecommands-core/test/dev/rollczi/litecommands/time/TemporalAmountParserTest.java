package dev.rollczi.litecommands.time;

import dev.rollczi.litecommands.time.TemporalAmountParser.LocalDateTimeProvider;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class TemporalAmountParserTest {

    @CsvSource({
        "0s,0",
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
    })
    @ParameterizedTest
    void testParse(String input, double expected) {
        TemporalAmountParser<Duration> temporalAmountParser = DurationParser.DATE_TIME_UNITS;
        Duration duration = temporalAmountParser.parse(input);

        assertEquals(expected, duration.toNanos() / 1_000_000_000.0);
    }

    @Test
    void testMonthParse() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTimeProvider basis = LocalDateTimeProvider.of(now);

        TemporalAmountParser<Duration> temporalAmountParser = DurationParser.DATE_TIME_UNITS
            .withLocalDateTimeProvider(basis);

        Duration duration = temporalAmountParser.parse("5mo");
        LocalDateTime feature = now.plusMonths(5);

        assertEquals(Duration.between(now, feature), duration);
    }

    @Test
    void testMonthParseFebruary() {
        LocalDateTime now = LocalDateTime.of(2021, Month.JANUARY, 31, 0, 0);
        LocalDateTimeProvider basis = LocalDateTimeProvider.of(now);

        TemporalAmountParser<Duration> temporalAmountParser = DurationParser.DATE_TIME_UNITS
            .withLocalDateTimeProvider(basis);

        Duration duration = temporalAmountParser.parse("1mo");
        LocalDateTime feature = LocalDateTime.of(2021, Month.FEBRUARY, 28, 0, 0);

        assertEquals(Duration.between(now, feature), duration);
    }

    @Test
    void testYearParse() {
        LocalDateTime now = LocalDateTime.of(2021, Month.JANUARY, 31, 0, 0);
        LocalDateTimeProvider basis = LocalDateTimeProvider.of(now);

        TemporalAmountParser<Duration> temporalAmountParser = DurationParser.DATE_TIME_UNITS
            .withLocalDateTimeProvider(basis);

        Duration duration = temporalAmountParser.parse("1y");
        LocalDateTime feature = LocalDateTime.of(2022, Month.JANUARY, 31, 0, 0);

        assertEquals(Duration.between(now, feature), duration);
    }

    @Test
    void testEmptyParse() {
        TemporalAmountParser<Duration> temporalAmountParser = DurationParser.DATE_TIME_UNITS;

        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.parse(""));
    }

    @Test
    void testNegativeParse() {
        TemporalAmountParser<Duration> temporalAmountParser = DurationParser.DATE_TIME_UNITS;

        assertEquals(Duration.ofMinutes(-1), temporalAmountParser.parse("-1m"));
        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.parse("1-1m"));
    }

    @Test
    void testIllegalCharacterParse() {
        TemporalAmountParser<Duration> temporalAmountParser = DurationParser.DATE_TIME_UNITS;

        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.parse("1#m2s3m"));
    }

    @Test
    void testMissingUnit() {
        TemporalAmountParser<Duration> temporalAmountParser = new DurationParser()
            .withUnit("s", ChronoUnit.SECONDS);

        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.parse("1d10m"));
    }

    @Test
    void testAlreadyExistingUnit() {
        assertThrows(IllegalArgumentException.class, () -> new DurationParser()
            .withUnit("s", ChronoUnit.SECONDS)
            .withUnit("s", ChronoUnit.SECONDS));
    }

    @Test
    void testInvalidUnit() {
        assertThrows(IllegalArgumentException.class, () -> new DurationParser()
            .withUnit("0", ChronoUnit.SECONDS));
    }

    @Test
    void testParseWithMonth() {
        TemporalAmountParser<Duration> temporalAmountParser = new DurationParser()
            .withLocalDateTimeProvider(() -> LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0, 0))
            .withUnit("mo", ChronoUnit.MONTHS);

        assertEquals(Duration.ofDays(31), temporalAmountParser.parse("1mo"));
        assertEquals(Duration.ofDays(59), temporalAmountParser.parse("2mo"));
        assertEquals(Duration.ofDays(90), temporalAmountParser.parse("3mo"));
        assertEquals(Duration.ofDays(365), temporalAmountParser.parse("12mo"));
    }

    @Test
    void testParseWithMonthWhenLeapYear() {
        TemporalAmountParser<Duration> temporalAmountParser = new DurationParser()
            .withLocalDateTimeProvider(() -> LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0))
            .withUnit("mo", ChronoUnit.MONTHS);

        assertEquals(Duration.ofDays(31), temporalAmountParser.parse("1mo"));
        assertEquals(Duration.ofDays(60), temporalAmountParser.parse("2mo"));
        assertEquals(Duration.ofDays(91), temporalAmountParser.parse("3mo"));
        assertEquals(Duration.ofDays(366), temporalAmountParser.parse("12mo"));
    }

    @Test
    void testParseInvalidEmptyNumber() {
        TemporalAmountParser<Duration> temporalAmountParser = new DurationParser()
            .withUnit("s", ChronoUnit.SECONDS);

        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.parse("s"));
    }

    @Test
    void testParseInvalidEmptyUnit() {
        TemporalAmountParser<Duration> temporalAmountParser = new DurationParser()
            .withUnit("s", ChronoUnit.SECONDS);

        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.parse("1"));
    }

    @Test
    void testParseInvalidToLageNumber() {
        TemporalAmountParser<Duration> temporalAmountParser = new DurationParser()
            .withUnit("s", ChronoUnit.SECONDS);

        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.parse("1000000000000000000000000000000000000000s"));
    }

    @CsvSource({
        "0,0s",
        "60,1m",
        "61,1m1s",
        "3600,1h",
        "86400,1d",
        "87000,1d10m",
        "87020,1d10m20s",
        "1814400,3w",
        "2592000,1mo",
        "31536000,1y",
        "-60,-1m",
        "-3660,-1h1m",
    })
    @ParameterizedTest
    void testFormat(int seconds, String expected) {
        TemporalAmountParser<Duration> temporalAmountParser = DurationParser.DATE_TIME_UNITS;

        Duration duration = Duration.ofSeconds(seconds);
        String formatted = temporalAmountParser.format(duration);

        assertEquals(expected, formatted);
    }

    @CsvSource({
        "0,0s",
        "60,1m",
        "61,1m",
        "3601,1h",
        "86400,1d",
        "87000,1d10m",
        "87020,1d10m",
        "1814402,3w",
        "2592010,1mo",
        "31536010,1y",
        "-60,-1m",
        "-61,-1m",
        "-3661,-1h1m",
    })
    @ParameterizedTest
    void testRoundedDownFormat(int seconds, String expected) {
        TemporalAmountParser<Duration> temporalAmountParser = DurationParser.DATE_TIME_UNITS
            .withRounded(ChronoUnit.MINUTES, RoundingMode.DOWN);
        String formatted = temporalAmountParser.format(Duration.ofSeconds(seconds));

        assertEquals(expected, formatted);
    }

    @CsvSource({
        "0,0s",
        "60,1m",
        "61,2m",
        "3601,1h1m",
        "86400,1d",
        "87000,1d10m",
        "87020,1d11m",
        "1814402,3w1m",
        "2592010,1mo1m",
        "31536010,1y1m",
        "-60,-1m",
        "-61,-2m",
        "-3661,-1h2m",
    })
    @ParameterizedTest
    void testRoundedUpFormat(int seconds, String expected) {
        TemporalAmountParser<Duration> temporalAmountParser = DurationParser.DATE_TIME_UNITS
            .withRounded(ChronoUnit.MINUTES, RoundingMode.UP);
        String formatted = temporalAmountParser.format(Duration.ofSeconds(seconds));

        assertEquals(expected, formatted);
    }

    @CsvSource({
        "0,0s",
        "60,1m",
        "61,2m",
        "3601,1h1m",
        "86400,1d",
        "87000,1d10m",
        "87020,1d11m",
        "1814402,3w1m",
        "2592010,1mo1m",
        "31536010,1y1m",
        "-60,-1m",
        "-61,-1m",
        "-3661,-1h1m",
    })
    @ParameterizedTest
    void testRoundedCeilingFormat(int seconds, String expected) {
        TemporalAmountParser<Duration> temporalAmountParser = DurationParser.DATE_TIME_UNITS
            .withRounded(ChronoUnit.MINUTES, RoundingMode.CEILING);
        String formatted = temporalAmountParser.format(Duration.ofSeconds(seconds));

        assertEquals(expected, formatted);
    }

    @CsvSource({
        "0,0s",
        "60,1m",
        "61,1m",
        "3601,1h",
        "86400,1d",
        "87000,1d10m",
        "87020,1d10m",
        "1814402,3w",
        "2592010,1mo",
        "31536010,1y",
        "-60,-1m",
        "-61,-2m",
        "-3661,-1h2m",
    })
    @ParameterizedTest
    void testRoundedFloorFormat(int seconds, String expected) {
        TemporalAmountParser<Duration> temporalAmountParser = DurationParser.DATE_TIME_UNITS
            .withRounded(ChronoUnit.MINUTES, RoundingMode.FLOOR);
        String formatted = temporalAmountParser.format(Duration.ofSeconds(seconds));

        assertEquals(expected, formatted);
    }

    @CsvSource({
        "0,0s",
        "60,1m",
        "89,1m",
        "90,2m",
        "91,2m",
        "3601,1h",
        "86400,1d",
        "87000,1d10m",
        "87030,1d11m",
        "1814402,3w",
        "2592010,1mo",
        "31536010,1y",
        "-60,-1m",
        "-89,-1m",
        "-90,-2m",
        "-91,-2m",
        "-3661,-1h1m",
    })
    @ParameterizedTest
    void testRoundedHalfUpFormat(int seconds, String expected) {
        TemporalAmountParser<Duration> temporalAmountParser = DurationParser.DATE_TIME_UNITS
            .withRounded(ChronoUnit.MINUTES, RoundingMode.HALF_UP);
        String formatted = temporalAmountParser.format(Duration.ofSeconds(seconds));

        assertEquals(expected, formatted);
    }

    @CsvSource({
        "0,0s",
        "60,1m",
        "89,1m",
        "90,1m",
        "91,2m",
        "3601,1h",
        "86400,1d",
        "87000,1d10m",
        "87030,1d10m",
        "87031,1d11m",
        "1814402,3w",
        "2592010,1mo",
        "31536010,1y",
        "-60,-1m",
        "-89,-1m",
        "-90,-1m",
        "-91,-2m",
        "-3661,-1h1m",
    })
    @ParameterizedTest
    void testRoundedHalfDownFormat(int seconds, String expected) {
        TemporalAmountParser<Duration> temporalAmountParser = DurationParser.DATE_TIME_UNITS
            .withRounded(ChronoUnit.MINUTES, RoundingMode.HALF_DOWN);
        String formatted = temporalAmountParser.format(Duration.ofSeconds(seconds));

        assertEquals(expected, formatted);
    }

    @Test
    void testNotSupportedChronoUnit() {
        TemporalAmountParser<Period> temporalAmountParser = new PeriodParser()
            .withUnit("mill", ChronoUnit.MILLENNIA);

        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.format(Period.ofYears(10000)));
    }

    @Test
    void testLocalDateTimeProvider() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTimeProvider normal = LocalDateTimeProvider.of(now);
        LocalDateTimeProvider locateDate = LocalDateTimeProvider.of(now.toLocalDate());
        LocalDateTimeProvider startOfToday = LocalDateTimeProvider.startOfToday();

        assertEquals(now, normal.get());
        assertEquals(now.toLocalDate().atStartOfDay(), locateDate.get());
        assertEquals(now.toLocalDate().atStartOfDay(), startOfToday.get());
    }

}