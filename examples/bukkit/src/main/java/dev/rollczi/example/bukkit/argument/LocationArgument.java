package dev.rollczi.example.bukkit.argument;


import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.type.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.List;

public class LocationArgument implements ArgumentResolver<CommandSender, Location> {

    @Override
    public Range getRange() {
        return Range.of(3);
    }

    @Override
    public ArgumentResult<Location> parse(Invocation<CommandSender> invocation, Argument<Location> argument, List<String> arguments) {
        try {
            double x = Double.parseDouble(arguments.get(0));
            double y = Double.parseDouble(arguments.get(1));
            double z = Double.parseDouble(arguments.get(2));

            return ArgumentResult.success(() -> new Location(null, x, y, z));
        }
        catch (NumberFormatException exception) {
            return ArgumentResult.failure("Invalid location");
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Location> argument, SuggestionContext suggestion) {
        return SuggestionResult.of(
            "100 100 100",
            "5 5 5",
            "10 35 -10"
        );
    }
}
