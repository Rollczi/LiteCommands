package dev.rollczi.litecommands.time;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class PeriodParser extends TemporalAmountParser<Period> {

    public static final TemporalAmountParser<Period> DATE_UNITS = new PeriodParser()
        .withUnit("s", ChronoUnit.SECONDS)
        .withUnit("m", ChronoUnit.MINUTES)
        .withUnit("h", ChronoUnit.HOURS)
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
    protected Period toTemporalAmount(LocalDateTimeProvider basisEstimation, Duration duration) {
        LocalDateTime localDate = basisEstimation.get();
        LocalDateTime estimatedDate = localDate.plus(duration);

        return Period.between(localDate.toLocalDate(), estimatedDate.toLocalDate());
    }

    @Override
    protected Duration toDuration(LocalDateTimeProvider basisEstimation, Period period) {
        LocalDateTime localDate = basisEstimation.get();
        LocalDateTime estimatedDate = localDate.plus(period);

        return Duration.between(localDate, estimatedDate);
    }

}
