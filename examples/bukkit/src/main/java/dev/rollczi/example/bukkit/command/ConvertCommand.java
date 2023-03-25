package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.route.Route;

import java.time.Duration;
import java.time.Instant;

@Route(name = "convert")
@Permission("dev.rollczi.convert")
public class ConvertCommand {

    private static final String CONVERSION_MESSAGE_TEMPLATE = "&7Convert: &f%s&7ms.";

    @Execute(route = "instant")
    public String convertInstant(@Arg Instant value) {
        return String.format(CONVERSION_MESSAGE_TEMPLATE, value.toEpochMilli());
    }

    @Execute(route = "duration")
    public String convertDuration(@Arg Duration value) {
        return String.format(CONVERSION_MESSAGE_TEMPLATE, value.toMillis());
    }

}
