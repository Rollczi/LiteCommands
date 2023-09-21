package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.execute.Execute;
import dev.rollczi.litecommands.permission.Permission;
import dev.rollczi.litecommands.command.Command;

import java.time.Duration;
import java.time.Instant;

@Command(name = "convert")
@Permission("dev.rollczi.convert")
public class ConvertCommand {

    private static final String CONVERSION_MESSAGE_TEMPLATE = "&7Convert: &f%s&7ms.";

    @Execute(name = "instant")
    public String convertInstant(@Arg Instant value) {
        return String.format(CONVERSION_MESSAGE_TEMPLATE, value.toEpochMilli());
    }

    @Execute(name = "duration")
    public String convertDuration(@Arg Duration value) {
        return String.format(CONVERSION_MESSAGE_TEMPLATE, value.toMillis());
    }

}
