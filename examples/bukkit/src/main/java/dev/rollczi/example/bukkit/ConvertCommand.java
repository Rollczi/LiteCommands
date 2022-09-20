package dev.rollczi.example.bukkit;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.section.Section;

import java.time.Duration;
import java.time.Instant;

@Section(route = "convert")
@Permission("dev.rollczi.convert")
public class ConvertCommand {

    private static final String CONVERSION_MESSAGE_TEMPLATE = "&7Wynik zamiany wskazanej przez ciebie wartości na milisekundy wynosi &f%s&7ms.";

    @Execute(route = "instant")
    public String convertInstant(@Arg Instant value) {
        return String.format(CONVERSION_MESSAGE_TEMPLATE, value.toEpochMilli());
    }

    @Execute(route = "duration")
    public String convertDuration(@Arg Duration value) {
        return String.format(CONVERSION_MESSAGE_TEMPLATE, value.toMillis());
    }

}
