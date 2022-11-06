package dev.rollczi.litecommands.argument.basictype.time;

import dev.rollczi.litecommands.shared.TemporalAmountParser;

import java.time.Duration;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

public class PeriodArgument extends TemporalAmountArgument<Period> {

    private static final List<Period> SUGGESTED_PERIODS = Arrays.asList(
        Period.ofDays(1),
        Period.ofDays(7),
        Period.ofDays(30),
        Period.ofMonths(1),
        Period.ofMonths(3),
        Period.ofMonths(6),
        Period.ofYears(1)
    );

    public PeriodArgument() {
        super(
            TemporalAmountParser.DATE_UNITS,
            () -> SUGGESTED_PERIODS
        );
    }

}
