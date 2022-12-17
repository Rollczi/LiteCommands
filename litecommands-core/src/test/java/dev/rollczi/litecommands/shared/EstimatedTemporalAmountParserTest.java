package dev.rollczi.litecommands.shared;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import dev.rollczi.litecommands.shared.EstimatedTemporalAmountParser.BasisForTimeEstimation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EstimatedTemporalAmountParserTest {

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
    })
    @ParameterizedTest
    void testParse(String input, double expected) {
        EstimatedTemporalAmountParser<Duration> temporalAmountParser = EstimatedTemporalAmountParser.DATE_TIME_UNITS;
        Duration duration = temporalAmountParser.parse(input);

        assertEquals(expected, duration.toNanos() / 1_000_000_000.0);
    }

    @Test
    void testMonthParse() {
        LocalDateTime now = LocalDateTime.now();
        BasisForTimeEstimation basis = BasisForTimeEstimation.of(now);

        EstimatedTemporalAmountParser<Duration> temporalAmountParser = EstimatedTemporalAmountParser.DATE_TIME_UNITS
            .withBasisForTimeEstimation(basis);

        Duration duration = temporalAmountParser.parse("5mo");
        LocalDateTime feature = now.plusMonths(5);

        assertEquals(Duration.between(now, feature), duration);
    }

    @Test
    void testMonthParseFebruary() {
        LocalDateTime now = LocalDateTime.of(2021, Month.JANUARY, 31, 0, 0);
        BasisForTimeEstimation basis = BasisForTimeEstimation.of(now);

        EstimatedTemporalAmountParser<Duration> temporalAmountParser = EstimatedTemporalAmountParser.DATE_TIME_UNITS
            .withBasisForTimeEstimation(basis);

        Duration duration = temporalAmountParser.parse("1mo");
        LocalDateTime feature = LocalDateTime.of(2021, Month.FEBRUARY, 28, 0, 0);

        assertEquals(Duration.between(now, feature), duration);
    }

    @Test
    void testYearParse() {
        LocalDateTime now = LocalDateTime.of(2021, Month.JANUARY, 31, 0, 0);
        BasisForTimeEstimation basis = BasisForTimeEstimation.of(now);

        EstimatedTemporalAmountParser<Duration> temporalAmountParser = EstimatedTemporalAmountParser.DATE_TIME_UNITS
            .withBasisForTimeEstimation(basis);

        Duration duration = temporalAmountParser.parse("1y");
        LocalDateTime feature = LocalDateTime.of(2022, Month.JANUARY, 31, 0, 0);

        assertEquals(Duration.between(now, feature), duration);
    }

    @Test
    void testEmptyParse() {
        EstimatedTemporalAmountParser<Duration> temporalAmountParser = EstimatedTemporalAmountParser.DATE_TIME_UNITS;

        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.parse(""));
    }

    @Test
    void testNegativeParse() {
        EstimatedTemporalAmountParser<Duration> temporalAmountParser = EstimatedTemporalAmountParser.DATE_TIME_UNITS;

        assertEquals(Duration.ofMinutes(-1), temporalAmountParser.parse("-1m"));
        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.parse("1-1m"));
    }

    @Test
    void testIllegalCharacterParse() {
        EstimatedTemporalAmountParser<Duration> temporalAmountParser = EstimatedTemporalAmountParser.DATE_TIME_UNITS;

        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.parse("1#m2s3m"));
    }

    @Test
    void testMissingUnit() {
        EstimatedTemporalAmountParser<Duration> temporalAmountParser = EstimatedTemporalAmountParser.createDuration()
            .withUnit("s", ChronoUnit.SECONDS);

        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.parse("1d10m"));
    }

    @Test
    void testAlreadyExistingUnit() {
        assertThrows(IllegalArgumentException.class, () -> EstimatedTemporalAmountParser.createDuration()
            .withUnit("s", ChronoUnit.SECONDS)
            .withUnit("s", ChronoUnit.SECONDS));
    }

    @Test
    void testInvalidUnit() {
        assertThrows(IllegalArgumentException.class, () -> EstimatedTemporalAmountParser.createDuration()
            .withUnit("0", ChronoUnit.SECONDS));
    }

    @Test
    void testParseWithMonth() {
        EstimatedTemporalAmountParser<Duration> temporalAmountParser = EstimatedTemporalAmountParser.createDuration()
            .withBasisForTimeEstimation(() -> LocalDateTime.of(2022, Month.JANUARY, 1, 0, 0, 0))
            .withUnit("mo", ChronoUnit.MONTHS);

        assertEquals(Duration.ofDays(31), temporalAmountParser.parse("1mo"));
        assertEquals(Duration.ofDays(59), temporalAmountParser.parse("2mo"));
        assertEquals(Duration.ofDays(90), temporalAmountParser.parse("3mo"));
        assertEquals(Duration.ofDays(365), temporalAmountParser.parse("12mo"));
    }

    @Test
    void testParseWithMonthWhenLeapYear() {
        EstimatedTemporalAmountParser<Duration> temporalAmountParser = EstimatedTemporalAmountParser.createDuration()
            .withBasisForTimeEstimation(() -> LocalDateTime.of(2020, Month.JANUARY, 1, 0, 0, 0))
            .withUnit("mo", ChronoUnit.MONTHS);

        assertEquals(Duration.ofDays(31), temporalAmountParser.parse("1mo"));
        assertEquals(Duration.ofDays(60), temporalAmountParser.parse("2mo"));
        assertEquals(Duration.ofDays(91), temporalAmountParser.parse("3mo"));
        assertEquals(Duration.ofDays(366), temporalAmountParser.parse("12mo"));
    }

    @Test
    void testParseInvalidEmptyNumber() {
        EstimatedTemporalAmountParser<Duration> temporalAmountParser = EstimatedTemporalAmountParser.createDuration()
            .withUnit("s", ChronoUnit.SECONDS);

        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.parse("s"));
    }

    @Test
    void testParseInvalidEmptyUnit() {
        EstimatedTemporalAmountParser<Duration> temporalAmountParser = EstimatedTemporalAmountParser.createDuration()
            .withUnit("s", ChronoUnit.SECONDS);

        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.parse("1"));
    }

    @Test
    void testParseInvalidToLageNumber() {
        EstimatedTemporalAmountParser<Duration> temporalAmountParser = EstimatedTemporalAmountParser.createDuration()
            .withUnit("s", ChronoUnit.SECONDS);

        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.parse("1000000000000000000000000000000000000000s"));
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
        "-60,-1m",
        "-3660,-1h1m",
    })
    @ParameterizedTest
    void testFormat(int seconds, String expected) {
        EstimatedTemporalAmountParser<Duration> temporalAmountParser = EstimatedTemporalAmountParser.DATE_TIME_UNITS;

        Duration duration = Duration.ofSeconds(seconds);
        String formatted = temporalAmountParser.format(duration);

        assertEquals(expected, formatted);
    }

    @Test
    void testNotSupportedChronoUnit() {
        EstimatedTemporalAmountParser<Period> temporalAmountParser = EstimatedTemporalAmountParser.createPeriod()
            .withUnit("mill", ChronoUnit.MILLENNIA);

        assertThrows(IllegalArgumentException.class, () -> temporalAmountParser.format(Period.ofYears(10000)));
    }

    @Test
    void testBasisForTimeEstimation() {
        LocalDateTime now = LocalDateTime.now();

        BasisForTimeEstimation normal = BasisForTimeEstimation.of(now);
        BasisForTimeEstimation locateDate = BasisForTimeEstimation.of(now.toLocalDate());
        BasisForTimeEstimation startOfToday = BasisForTimeEstimation.startOfToday();

        assertEquals(now, normal.get());
        assertEquals(now.toLocalDate().atStartOfDay(), locateDate.get());
        assertEquals(now.toLocalDate().atStartOfDay(), startOfToday.get());
    }

}
