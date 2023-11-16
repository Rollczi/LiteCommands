package dev.rollczi.litecommands.time;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class DurationParser extends TemporalAmountParser<Duration> {

    public static final TemporalAmountParser<Duration> TIME_UNITS = new DurationParser()
        .withUnit("ms", ChronoUnit.MILLIS)
        .withUnit("s", ChronoUnit.SECONDS)
        .withUnit("m", ChronoUnit.MINUTES)
        .withUnit("h", ChronoUnit.HOURS);

    public static final TemporalAmountParser<Duration> DATE_TIME_UNITS = new DurationParser()
        .withUnit("ns", ChronoUnit.NANOS)
        .withUnit("us", ChronoUnit.MICROS)
        .withUnit("ms", ChronoUnit.MILLIS)
        .withUnit("s", ChronoUnit.SECONDS)
        .withUnit("m", ChronoUnit.MINUTES)
        .withUnit("h", ChronoUnit.HOURS)
        .withUnit("d", ChronoUnit.DAYS)
        .withUnit("w", ChronoUnit.WEEKS)
        .withUnit("mo", ChronoUnit.MONTHS)
        .withUnit("y", ChronoUnit.YEARS);

    public DurationParser() {
        super(LocalDateTimeProvider.now());
    }

    public DurationParser(LocalDateTimeProvider localDateTimeProvider) {
        super(localDateTimeProvider);
    }

    private DurationParser(Map<String, ChronoUnit> units, LocalDateTimeProvider baseForTimeEstimation) {
        super(units, baseForTimeEstimation);
    }

    @Override
    protected TemporalAmountParser<Duration> clone(Map<String, ChronoUnit> units, LocalDateTimeProvider baseForTimeEstimation) {
        return new DurationParser(units, baseForTimeEstimation);
    }

    @Override
    protected Duration plus(LocalDateTimeProvider baseForTimeEstimation, Duration temporalAmount, TemporalEntry temporalEntry) {
        if (temporalEntry.getUnit().isDurationEstimated()) {
            LocalDateTime baseDateTime = baseForTimeEstimation.get();
            LocalDateTime estimatedDateTime = baseDateTime.plus(temporalEntry.getCount(), temporalEntry.getUnit());
            Duration estimatedDuration = Duration.between(baseDateTime, estimatedDateTime);

            return temporalAmount.plus(estimatedDuration);
        }

        return temporalAmount.plus(temporalEntry.getCount(), temporalEntry.getUnit());
    }


    @Override
    protected Duration negate(Duration temporalAmount) {
        return temporalAmount.negated();
    }

    @Override
    protected Duration getZero() {
        return Duration.ZERO;
    }

    @Override
    protected Duration toDuration(LocalDateTimeProvider baseForTimeEstimation, Duration temporalAmount) {
        return temporalAmount;
    }

}
