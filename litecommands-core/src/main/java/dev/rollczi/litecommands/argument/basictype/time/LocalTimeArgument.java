package dev.rollczi.litecommands.argument.basictype.time;

import java.time.LocalTime;

public class LocalTimeArgument extends TemporalAccessorArgument<LocalTime> {

    public LocalTimeArgument() {
        super("HH:mm:ss",
            LocalTime::from,
            () -> TemporalUtils.allHoursOfDay(LocalTime.now())
        );
    }

}
