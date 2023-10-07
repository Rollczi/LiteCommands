package dev.rollczi.litecommands.velocity.tools;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import java.util.List;
import panda.std.Option;
import panda.std.Result;

@ArgumentName("player")
public class VelocityPlayerArgument<T> implements OneArgument<Player> {

    private final ProxyServer server;
    private final T playerNotFoundMessage;

    public VelocityPlayerArgument(ProxyServer server, T playerNotFoundMessage) {
        this.server = server;
        this.playerNotFoundMessage = playerNotFoundMessage;
    }

    @Override
    public Result<Player, Object> parse(LiteInvocation invocation, String argument) {
        return Option.ofOptional(this.server.getPlayer(argument)).toResult(playerNotFoundMessage);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return server.getAllPlayers().stream()
            .map(Player::getUsername)
            .map(Suggestion::of)
            .toList();
    }
}
