package dev.rollczi.litecommands.argument.basictype.time;

import java.time.chrono.ThaiBuddhistChronology;
import java.time.chrono.ThaiBuddhistDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ThaiBuddhistDateArgument extends TemporalAccessorArgument<ThaiBuddhistDate> {

    public ThaiBuddhistDateArgument() {
        super(DateTimeFormatter.ofPattern("GGGG yyyy-MM-dd", Locale.ROOT).withChronology(ThaiBuddhistChronology.INSTANCE),
            ThaiBuddhistDate::from,
            () -> TemporalUtils.allDaysOfWeek(ThaiBuddhistDate.now())
        );
    }

}
