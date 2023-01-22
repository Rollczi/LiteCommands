package dev.rollczi.litecommands.minestom.tools;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import net.minestom.server.entity.Player;
import net.minestom.server.network.ConnectionManager;
import panda.std.Option;
import panda.std.Result;

import java.util.List;
import java.util.stream.Collectors;

@ArgumentName("player")
public class MinestomPlayerArgument<MESSAGE> implements OneArgument<Player> {

    private final ConnectionManager connectionManager;
    private final MESSAGE playerNotFoundMessage;

    public MinestomPlayerArgument(ConnectionManager connectionManager, MESSAGE playerNotFoundMessage) {
        this.connectionManager = connectionManager;
        this.playerNotFoundMessage = playerNotFoundMessage;
    }

    @Override
    public Result<Player, Object> parse(LiteInvocation invocation, String argument) {
        return Option.of(this.connectionManager.getPlayer(argument)).toResult(this.playerNotFoundMessage);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return this.connectionManager.getOnlinePlayers().stream()
                .map(Player::getUsername)
                .map(Suggestion::of)
                .collect(Collectors.toList());
    }

}
