package dev.rollczi.example.paper.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class GameModeArgument extends ArgumentResolver<CommandSender, GameMode> {

    private static final Map<String, GameMode> GAME_MODE_ARGUMENTS = new HashMap<>();

    static {
        for (GameMode value : GameMode.values()) {
            GAME_MODE_ARGUMENTS.put(value.name().toLowerCase(), value);
            //noinspection deprecation
            GAME_MODE_ARGUMENTS.put(String.valueOf(value.getValue()), value);
        }
    }

    @Override
    protected ParseResult<GameMode> parse(Invocation<CommandSender> invocation, Argument<GameMode> context, String argument) {
        GameMode gameMode = GAME_MODE_ARGUMENTS.get(argument.toLowerCase());

        if (gameMode == null) {
            return ParseResult.failure("Invalid gamemode argument!");
        }

        return ParseResult.success(gameMode);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<GameMode> argument, SuggestionContext context) {
        return SuggestionResult.of(GAME_MODE_ARGUMENTS.keySet());
    }

}
