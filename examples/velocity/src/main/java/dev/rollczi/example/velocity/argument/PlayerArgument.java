package dev.rollczi.example.velocity.argument;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.suggestion.SuggestionStream;

import java.util.Optional;

public class PlayerArgument extends ArgumentResolver<CommandSource, Player> {

    private final ProxyServer server;

    public PlayerArgument(ProxyServer server) {
        this.server = server;
    }

    @Override
    protected ParseResult<Player> parse(Invocation<CommandSource> invocation, Argument<Player> context, String argument) {
        Optional<Player> optionalPlayer = this.server.getPlayer(argument);

        return optionalPlayer.map(player -> ParseResult.success(player))
            .orElseGet(() -> ParseResult.failure("Player " + argument + " not found"));

    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSource> invocation, Argument<Player> argument, SuggestionContext context) {
        return SuggestionStream.of(this.server.getAllPlayers())
            .collect(player -> player.getUsername());
    }

}
