package dev.rollczi.litecommands.time;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

public class PeriodParser extends TemporalAmountParser<Period> {

    public static final TemporalAmountParser<Period> DATE_UNITS = new PeriodParser()
        .withUnit("d", ChronoUnit.DAYS)
        .withUnit("w", ChronoUnit.WEEKS)
        .withUnit("mo", ChronoUnit.MONTHS)
        .withUnit("y", ChronoUnit.YEARS);

    public PeriodParser() {
        super(ChronoUnit.DAYS, LocalDateTimeProvider.now());
    }

    public PeriodParser(ChronoUnit defaultZero) {
        super(defaultZero, LocalDateTimeProvider.now());
    }

    public PeriodParser(LocalDateTimeProvider baseForTimeEstimation) {
        super(ChronoUnit.DAYS, baseForTimeEstimation);
    }


    public PeriodParser(ChronoUnit defaultZero, LocalDateTimeProvider baseForTimeEstimation) {
        super(defaultZero, baseForTimeEstimation);
    }

    private PeriodParser(ChronoUnit defaultZero, Map<String, ChronoUnit> units, Set<TimeModifier> modifiers, LocalDateTimeProvider baseForTimeEstimation) {
        super(defaultZero, units, modifiers, baseForTimeEstimation);
    }

    @Override
    protected TemporalAmountParser<Period> clone(ChronoUnit defaultZero, Map<String, ChronoUnit> units, Set<TimeModifier> modifiers, LocalDateTimeProvider baseForTimeEstimation) {
        return new PeriodParser(defaultZero, units, modifiers, baseForTimeEstimation);
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
