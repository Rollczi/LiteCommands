package dev.rollczi.example.bukkit.argument;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import org.bukkit.GameMode;
import panda.std.Result;

import java.util.HashMap;
import java.util.Map;

@ArgumentName("gamemode")
public class GameModeArgument implements OneArgument<GameMode> {

    private static final Map<String, GameMode> GAME_MODE_ARGUMENTS = new HashMap<>();

    static {
        for (GameMode value : GameMode.values()) {
            GAME_MODE_ARGUMENTS.put(value.name().toLowerCase(), value);
            GAME_MODE_ARGUMENTS.put(String.valueOf(value.getValue()), value);
        }
    }

    @Override
    public Result<GameMode, ?> parse(LiteInvocation invocation, String argument) {
        GameMode gameMode = GAME_MODE_ARGUMENTS.get(argument.toLowerCase());

        if (gameMode == null) {
            return Result.error("Invalid gamemode");
        }

        return Result.ok(gameMode);
    }
}
