package dev.rollczi.litecommands.argument.basictype.time;

import java.time.Month;
import java.util.Arrays;

public class MonthArgument extends TemporalAccessorArgument<Month> {

    public MonthArgument() {
        super("MM",
            Month::from,
            () -> Arrays.asList(Month.values())
        );
    }

}
