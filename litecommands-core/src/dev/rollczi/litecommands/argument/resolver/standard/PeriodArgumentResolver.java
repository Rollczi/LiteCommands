package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.time.PeriodParser;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

public class PeriodArgumentResolver<SENDER> extends TemporalAmountArgumentResolver<SENDER, Period> {

    private static final List<String> SUGGESTIONS_LIST = Arrays.asList(
        "1d", "7d", "30d",
        "1mo", "3mo", "6mo",
        "1y", "3y", "6y"
    );

    public PeriodArgumentResolver() {
        super(PeriodParser.DATE_UNITS, () -> SUGGESTIONS_LIST);
    }
}
