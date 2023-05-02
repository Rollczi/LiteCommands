package dev.rollczi.example.velocity.argument;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.resolver.OneArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.suggestion.SuggestionStream;

import java.util.Optional;

public class PlayerArgument extends OneArgumentResolver<CommandSource, Player> {

    private final ProxyServer server;

    public PlayerArgument(ProxyServer server) {
        this.server = server;
    }

    @Override
    protected ArgumentResult<Player> parse(Invocation<CommandSource> invocation, Argument<Player> context, String argument) {
        Optional<Player> optionalPlayer = this.server.getPlayer(argument);

        return optionalPlayer.map(player -> ArgumentResult.success(player))
            .orElseGet(() -> ArgumentResult.failure("Player " + argument + " not found"));

    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSource> invocation, Argument<Player> argument, SuggestionContext suggestion) {
        return SuggestionStream.of(this.server.getAllPlayers())
            .collect(player -> player.getUsername());
    }

}
