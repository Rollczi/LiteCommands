package dev.rollczi.litecommands.argument.basictype.time;

import java.time.YearMonth;

public class YearMonthArgument extends TemporalAccessorArgument<YearMonth> {

    public YearMonthArgument() {
        super("yyyy-MM",
            YearMonth::from,
            () -> TemporalUtils.allYearsOfDecade(YearMonth.now())
        );
    }

}
