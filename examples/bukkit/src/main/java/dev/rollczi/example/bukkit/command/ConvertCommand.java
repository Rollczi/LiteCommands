package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.shortcut.Shortcut;
import dev.rollczi.litecommands.annotations.varargs.Varargs;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Command(name = "convert")
@Permission("dev.rollczi.convert")
public class ConvertCommand {

    private static final String CONVERSION_MESSAGE_TEMPLATE = "&7Convert: &f%s&7ms.";

    @Execute(name = "instant")
    @Shortcut("conv-instant")
    public String convertInstant(@Arg("time") Instant value) {
        return String.format(CONVERSION_MESSAGE_TEMPLATE, value.toEpochMilli());
    }

    @Execute(name = "duration")
    @Shortcut("conv-duration")
    public String convertDuration(@Arg("duration") Duration value) {
        return String.format(CONVERSION_MESSAGE_TEMPLATE, value.toMillis());
    }

    @Execute(name = "instant-multi")
    @Shortcut("conv-instant-multi")
    public String convertInstant(@Varargs(value = "times", delimiter = ",") List<Instant> instants, @Arg("duration") Duration duration) {
        StringBuilder builder = new StringBuilder();

        for (Instant instant : instants) {
            builder.append(String.format(CONVERSION_MESSAGE_TEMPLATE, instant.toEpochMilli()));
        }

        return builder.toString();
    }


    @Execute(name = "duration-multi")
    @Shortcut("conv-duration-multi")
    public String convertDurations(@Varargs(value = "times", delimiter = ", ") List<Duration> durations, @Arg("instant") Instant instant) {
        StringBuilder builder = new StringBuilder();

        for (Duration duration : durations) {
            builder.append(String.format(CONVERSION_MESSAGE_TEMPLATE, duration.toMillis()));
        }

        return builder.toString();
    }

}
