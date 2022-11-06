package dev.rollczi.litecommands.argument.basictype.time;

import java.time.Year;

public class YearArgument extends TemporalAccessorArgument<Year> {

    public YearArgument() {
        super("yyyy",
            Year::from,
            () -> TemporalUtils.allYearsOfDecade(Year.now())
        );
    }

}
