package dev.rollczi.example.bukkit.argument;

import dev.rollczi.litecommands.modern.argument.Argument;
import dev.rollczi.litecommands.modern.argument.ArgumentResult;
import dev.rollczi.litecommands.modern.argument.type.OneArgumentResolver;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.suggestion.SuggestionContext;
import dev.rollczi.litecommands.modern.suggestion.SuggestionResult;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import panda.std.Result;

import java.util.HashMap;
import java.util.Map;

public class GameModeArgument extends OneArgumentResolver<CommandSender, GameMode> {

    private static final Map<String, GameMode> GAME_MODE_ARGUMENTS = new HashMap<>();

    static {
        for (GameMode value : GameMode.values()) {
            GAME_MODE_ARGUMENTS.put(value.name().toLowerCase(), value);
            GAME_MODE_ARGUMENTS.put(String.valueOf(value.getValue()), value);
        }
    }

    @Override
    protected ArgumentResult<GameMode> parse(Invocation<CommandSender> invocation, Argument<GameMode> context, String argument) {
        GameMode gameMode = GAME_MODE_ARGUMENTS.get(argument.toLowerCase());

        if (gameMode == null) {
            return ArgumentResult.failure("Invalid gamemode argument!");
        }

        return ArgumentResult.success(gameMode);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<GameMode> argument, SuggestionContext suggestion) {
        return SuggestionResult.of(GAME_MODE_ARGUMENTS.keySet());
    }

}
