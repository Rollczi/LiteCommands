package dev.rollczi.litecommands.velocity.argument;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.velocity.LiteVelocityMessages;

import java.util.stream.Collectors;

public class PlayerArgument extends ArgumentResolver<CommandSource, Player> {

    private final MessageRegistry<CommandSource> messageRegistry;
    private final ProxyServer server;

    public PlayerArgument(MessageRegistry<CommandSource> messageRegistry, ProxyServer server) {
        this.messageRegistry = messageRegistry;
        this.server = server;
    }

    @Override
    protected ParseResult<Player> parse(Invocation<CommandSource> invocation, Argument<Player> context, String argument) {
        return this.server.getPlayer(argument)
            .map(parsed -> ParseResult.success(parsed))
            .orElseGet(() -> ParseResult.failure(messageRegistry.getInvoked(LiteVelocityMessages.PLAYER_NOT_FOUND, invocation, argument)));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSource> invocation, Argument<Player> argument, SuggestionContext context) {
        return SuggestionResult.from(
            this.server.getAllPlayers().stream()
                .map(player -> Suggestion.of(player.getUsername(), player.getUniqueId().toString()))
                .collect(Collectors.toList())
        );
    }

}
