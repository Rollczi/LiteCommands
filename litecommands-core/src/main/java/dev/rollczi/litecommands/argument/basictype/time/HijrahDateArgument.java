package dev.rollczi.litecommands.argument.basictype.time;

import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HijrahDateArgument extends TemporalAccessorArgument<HijrahDate> {

    public HijrahDateArgument() {
        super(DateTimeFormatter.ofPattern("GGGG yyyy-MM-dd", Locale.ROOT).withChronology(HijrahChronology.INSTANCE),
            HijrahDate::from,
            () -> TemporalUtils.allDaysOfWeek(HijrahDate.now())
        );
    }

}
