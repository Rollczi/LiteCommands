package dev.rollczi.litecommands.minestom.tools;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import panda.std.Option;
import panda.std.Result;

import java.util.List;
import java.util.stream.Collectors;

@ArgumentName("player")
public class MinestomPlayerArgument<MESSAGE> implements OneArgument<Player> {

    private final MESSAGE playerNotFoundMessage;

    public MinestomPlayerArgument(MESSAGE playerNotFoundMessage) {
        this.playerNotFoundMessage = playerNotFoundMessage;
    }

    @Override
    public Result<Player, Object> parse(LiteInvocation invocation, String argument) {
        return Option.of(MinecraftServer.getConnectionManager().getPlayer(argument)).toResult(playerNotFoundMessage);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
                .map(Player::getUsername)
                .map(Suggestion::of)
                .collect(Collectors.toList());
    }

}
