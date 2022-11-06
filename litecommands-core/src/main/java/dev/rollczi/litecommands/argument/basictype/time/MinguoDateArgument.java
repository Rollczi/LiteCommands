package dev.rollczi.litecommands.argument.basictype.time;

import java.time.chrono.MinguoChronology;
import java.time.chrono.MinguoDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MinguoDateArgument extends TemporalAccessorArgument<MinguoDate> {

    public MinguoDateArgument() {
        super(DateTimeFormatter.ofPattern("GGGG yyyy-MM-dd", Locale.ROOT).withChronology(MinguoChronology.INSTANCE),
            MinguoDate::from,
            () -> TemporalUtils.allDaysOfWeek(MinguoDate.now())
        );
    }

}
