package dev.rollczi.litecommands.bungee.tools;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import java.util.List;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import panda.std.Option;
import panda.std.Result;

@ArgumentName("player")
public class BungeePlayerArgument<T> implements OneArgument<ProxiedPlayer> {

    private final ProxyServer server;
    private final T playerNotFoundMessage;

    public BungeePlayerArgument(ProxyServer server, T playerNotFoundMessage) {
        this.server = server;
        this.playerNotFoundMessage = playerNotFoundMessage;
    }

    @Override
    public Result<ProxiedPlayer, Object> parse(LiteInvocation invocation, String argument) {
        return Option.of(this.server.getPlayer(argument)).toResult(playerNotFoundMessage);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return server.getPlayers().stream()
            .map(ProxiedPlayer::getName)
            .map(Suggestion::of)
            .collect(Collectors.toList());
    }
}
