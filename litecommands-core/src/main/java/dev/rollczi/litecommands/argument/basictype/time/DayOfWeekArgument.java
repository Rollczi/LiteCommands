package dev.rollczi.litecommands.argument.basictype.time;

import java.time.DayOfWeek;
import java.util.Arrays;

public class DayOfWeekArgument extends TemporalAccessorArgument<DayOfWeek> {

    public DayOfWeekArgument() {
        super("E",
            DayOfWeek::from,
            () -> Arrays.asList(DayOfWeek.values())
        );
    }

}
