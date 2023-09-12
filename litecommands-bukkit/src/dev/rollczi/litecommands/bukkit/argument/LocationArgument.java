package dev.rollczi.litecommands.bukkit.argument;


import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.argument.resolver.MultipleArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.List;

public class LocationArgument implements MultipleArgumentResolver<CommandSender, Location> {

    private final MessageRegistry messageRegistry;

    public LocationArgument(MessageRegistry messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public Range getRange(Argument<Location> argument) {
        return Range.of(3);
    }

    @Override
    public ParseResult<Location> parse(Invocation<CommandSender> invocation, Argument<Location> argument, RawInput rawInput) {
        String input = String.join(" ", rawInput.seeNext(3));

        try {
            double x = rawInput.nextDouble();
            double y = rawInput.nextDouble();
            double z = rawInput.nextDouble();

            return ParseResult.success(new Location(null, x, y, z));
        }
        catch (NumberFormatException exception) {
            return ParseResult.failure(this.messageRegistry.get(LiteBukkitMessages.LOCATION_INVALID_FORMAT, input));
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Location> argument, SuggestionContext context) {
        return SuggestionResult.of(
            "100 100 100",
            "5 5 5",
            "10 35 -10"
        );
    }

}
