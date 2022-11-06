package dev.rollczi.litecommands.argument.basictype.time;

import panda.std.stream.PandaStream;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final class TemporalUtils {

    private static final int WEEK_DAYS = 7;
    private static final int HOURS_IN_DAY = 24;
    private static final int YEARS_IN_DECADE = 10;

    private TemporalUtils() {
    }

    static <T extends Temporal> List<T> allDaysOfWeek(T start) {
        return generateTemporal(start, ChronoUnit.DAYS, WEEK_DAYS);
    }

    static <T extends Temporal> List<T> allHoursOfDay(T start) {
        return generateTemporal(start, ChronoUnit.HOURS, HOURS_IN_DAY);
    }

    static <T extends Temporal> List<T> allYearsOfDecade(T start) {
        return generateTemporal(start, ChronoUnit.YEARS, YEARS_IN_DECADE);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Temporal> List<T> generateTemporal(T start, ChronoUnit unit, int countGenerate) {
        Class<T> temporalClass = (Class<T>) start.getClass();

        return PandaStream.of(IntStream.range(0, countGenerate).boxed())
            .map(temporalCount -> start.plus(temporalCount, unit))
            .is(temporalClass)
            .collect(Collectors.toList());
    }

}
