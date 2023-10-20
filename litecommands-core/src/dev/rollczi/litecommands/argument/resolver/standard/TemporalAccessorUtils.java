package dev.rollczi.litecommands.argument.resolver.standard;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.YEARS;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class TemporalAccessorUtils {

    private static final int WEEK_DAYS = 7;
    private static final int HOURS_IN_DAY = 24;
    private static final int YEARS_IN_DECADE = 10;

    private TemporalAccessorUtils() {

    }

    static <T extends Temporal> List<T> allDaysOfWeek(T start) {
        return generateTemporal(start, DAYS, WEEK_DAYS);
    }

    static <T extends Temporal> List<T> allHoursOfDay(T start) {
        return generateTemporal(start, HOURS, HOURS_IN_DAY);
    }

    static <T extends Temporal> List<T> allYearsOfDecade(T start) {
        return generateTemporal(start, YEARS, YEARS_IN_DECADE);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Temporal> List<T> generateTemporal(T start, ChronoUnit unit, int countGenerate) {
        Class<T> temporalClass = (Class<T>) start.getClass();
        return IntStream.range(0, countGenerate).boxed()
            .map(temporalCount -> start.plus(temporalCount, unit))
            .filter(temporalClass::isInstance)
            .map(temporalClass::cast)
            .collect(Collectors.toList());
    }
}
