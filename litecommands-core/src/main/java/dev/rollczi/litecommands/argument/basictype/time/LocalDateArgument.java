package dev.rollczi.litecommands.argument.basictype.time;

import java.time.LocalDate;

public class LocalDateArgument extends TemporalAccessorArgument<LocalDate> {

    public LocalDateArgument() {
        super("yyyy-MM-dd",
            LocalDate::from,
            () -> TemporalUtils.allDaysOfWeek(LocalDate.now())
        );
    }

}
