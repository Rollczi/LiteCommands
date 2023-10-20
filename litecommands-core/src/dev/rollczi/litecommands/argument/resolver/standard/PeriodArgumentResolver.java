package dev.rollczi.litecommands.argument.resolver.standard;

import dev.rollczi.litecommands.time.PeriodParser;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

public class PeriodArgumentResolver<SENDER> extends TemporalAmountArgumentResolver<SENDER, Period> {

    private static final List<String> SUGGESTIONS_LIST = Arrays.asList(
        "1h", "3h", "7h",
        "1d", "7d", "30d",
        "1m", "3m", "6m", "1y", "5y"
    );

    public PeriodArgumentResolver() {
        super(PeriodParser.DATE_UNITS, () -> SUGGESTIONS_LIST);
    }
}
