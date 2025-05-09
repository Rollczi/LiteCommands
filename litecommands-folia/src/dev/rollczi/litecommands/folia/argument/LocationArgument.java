package dev.rollczi.litecommands.folia.argument;


import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.MultipleArgumentResolver;
import dev.rollczi.litecommands.folia.LiteFoliaMessages;
import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.input.raw.RawInput;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.range.Range;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationArgument implements MultipleArgumentResolver<CommandSender, Location> {

    private final static String CURRENT_LOCATION = "~";
    public static final String CORDINATE_FORMAT = "%.2f";

    private final MessageRegistry<CommandSender> messageRegistry;

    public LocationArgument(MessageRegistry<CommandSender> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public Range getRange(Argument<Location> argument) {
        return Range.of(LocationAxis.SIZE);
    }

    @Override
    public ParseResult<Location> parse(Invocation<CommandSender> invocation, Argument<Location> argument, RawInput rawInput) {
        String input = String.join(RawCommand.COMMAND_SEPARATOR, rawInput.seeNext(LocationAxis.SIZE));

        try {
            double x = parseAxis(invocation, rawInput.next(), LocationAxis.X);
            double y = parseAxis(invocation, rawInput.next(), LocationAxis.Y);
            double z = parseAxis(invocation, rawInput.next(), LocationAxis.Z);

            return ParseResult.success(new Location(null, x, y, z));
        } catch (NumberFormatException exception) {
            return ParseResult.failure(this.messageRegistry.getInvoked(LiteFoliaMessages.LOCATION_INVALID_FORMAT, invocation, input));
        }
    }

    private double parseAxis(Invocation<CommandSender> invocation, String input, LocationAxis axis) {
        try {
            return Double.parseDouble(input.replace(",", "."));
        } catch (NumberFormatException exception) {
            if (!input.equals(CURRENT_LOCATION)) {
                throw exception;
            }

            if (!(invocation.sender() instanceof Player)) {
                throw exception;
            }

            Player player = (Player) invocation.sender();
            Location location = player.getLocation();

            return axis.getValue(location);
        }
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Location> argument, SuggestionContext context) {
        Suggestion current = context.getCurrent();

        if (!isParsable(current)) {
            return SuggestionResult.empty();
        }

        CommandSender sender = invocation.sender();
        String firstPart = current.multilevel();
        SuggestionResult result = SuggestionResult.empty();

        if (!(sender instanceof Player)) {
            return SuggestionResult.of(firstPart);
        }

        Player player = (Player) sender;
        Location location = player.getLocation();

        List<String> currentSuggestion = suggestionsWithoutLast(current);
        List<String> dynamicSuggestion = suggestionsWithoutLast(current);

        if (currentSuggestion.size() == LocationAxis.SIZE) {
            currentSuggestion.remove(LocationAxis.SIZE - 1);
            dynamicSuggestion.remove(LocationAxis.SIZE - 1);
        }

        for (int axisIndex = currentSuggestion.size(); axisIndex < LocationAxis.SIZE; axisIndex++) {
            LocationAxis axis = LocationAxis.at(axisIndex);

            currentSuggestion.add(CURRENT_LOCATION);
            dynamicSuggestion.add(String.format(Locale.US, CORDINATE_FORMAT, axis.getValue(location)));

            result.add(Suggestion.from(currentSuggestion));
            result.add(Suggestion.from(dynamicSuggestion));
        }

        return result;
    }

    private boolean isParsable(Suggestion current) {
        List<String> arguments = suggestionsWithoutLast(current);

        for (String arg : arguments) {
            if (arg.equals(CURRENT_LOCATION)) {
                continue;
            }

            try {
                Double.parseDouble(arg);
            } catch (NumberFormatException exception) {
                return false;
            }
        }

        return true;
    }

    private List<String> suggestionsWithoutLast(Suggestion suggestion) {
        List<String> suggestionList = new ArrayList<>(suggestion.multilevelList());

        if (!suggestion.lastLevel().isEmpty()) {
            return suggestionList;
        }

        suggestionList.remove(suggestionList.size() - 1);
        return suggestionList;
    }

}
