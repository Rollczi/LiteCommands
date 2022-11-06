package dev.rollczi.litecommands.argument.basictype.time;

import java.time.ZonedDateTime;

public class ZonedDateTimeArgument extends TemporalAccessorArgument<ZonedDateTime> {

    public ZonedDateTimeArgument() {
        super("yyyy-MM-dd HH:mm:ss z",
            ZonedDateTime::from,
            () -> TemporalUtils.allDaysOfWeek(ZonedDateTime.now())
        );
    }

}
