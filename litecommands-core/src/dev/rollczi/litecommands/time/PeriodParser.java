package dev.rollczi.litecommands.time;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class PeriodParser extends TemporalAmountParser<Period> {

    public static final TemporalAmountParser<Period> DATE_UNITS = new PeriodParser()
        .withUnit("d", ChronoUnit.DAYS)
        .withUnit("w", ChronoUnit.WEEKS)
        .withUnit("mo", ChronoUnit.MONTHS)
        .withUnit("y", ChronoUnit.YEARS);

    public PeriodParser() {
        super(LocalDateTimeProvider.now());
    }

    public PeriodParser(LocalDateTimeProvider baseForTimeEstimation) {
        super(baseForTimeEstimation);
    }

    private PeriodParser(Map<String, ChronoUnit> units, LocalDateTimeProvider baseForTimeEstimation) {
        super(units, baseForTimeEstimation);
    }

    @Override
    protected TemporalAmountParser<Period> clone(Map<String, ChronoUnit> units, LocalDateTimeProvider baseForTimeEstimation) {
        return new PeriodParser(units, baseForTimeEstimation);
    }

    @Override
    protected Period plus(LocalDateTimeProvider baseForTimeEstimation, Period temporalAmount, TemporalEntry temporalEntry) {
        int count = (int) temporalEntry.getCount();
        ChronoUnit unit = temporalEntry.getUnit();

        if (unit == ChronoUnit.DAYS) {
            return temporalAmount.plus(Period.ofDays(count));
        }

        if (unit == ChronoUnit.WEEKS) {
            return temporalAmount.plus(Period.ofWeeks(count));
        }

        if (unit == ChronoUnit.MONTHS) {
            return temporalAmount.plus(Period.ofMonths(count));
        }

        if (unit == ChronoUnit.YEARS) {
            return temporalAmount.plus(Period.ofYears(count));
        }

        throw new IllegalArgumentException("Unsupported unit: " + unit);
    }

    @Override
    protected Period negate(Period temporalAmount) {
        return temporalAmount.negated();
    }

    @Override
    protected Period getZero() {
        return Period.ZERO;
    }

    @Override
    protected Duration toDuration(LocalDateTimeProvider basisEstimation, Period period) {
        LocalDateTime localDate = basisEstimation.get();
        LocalDateTime estimatedDate = localDate.plus(period);

        return Duration.between(localDate, estimatedDate);
    }

}
