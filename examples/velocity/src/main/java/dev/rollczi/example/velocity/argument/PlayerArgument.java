package dev.rollczi.example.velocity.argument;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Option;
import panda.std.Result;

import java.util.List;
import java.util.stream.Collectors;

@ArgumentName("player")
public class PlayerArgument implements OneArgument<Player> {

    private final ProxyServer server;

    public PlayerArgument(ProxyServer server) {
        this.server = server;
    }

    @Override
    public Result<Player,?> parse(LiteInvocation liteInvocation, String argument) {
        return Option.ofOptional(this.server.getPlayer(argument)).toResult("Player not found");

    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return server.getAllPlayers().stream()
            .map(Player::getUsername)
            .map(Suggestion::of)
            .collect(Collectors.toList());
    }

}
