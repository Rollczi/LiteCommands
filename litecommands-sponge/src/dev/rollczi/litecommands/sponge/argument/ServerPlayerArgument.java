package dev.rollczi.litecommands.sponge.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.sponge.LiteSpongeMessages;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class ServerPlayerArgument extends ArgumentResolver<CommandCause, ServerPlayer> {

    private final Game game;
    private final MessageRegistry<CommandCause> messageRegistry;

    public ServerPlayerArgument(Game game, MessageRegistry<CommandCause> messageRegistry) {
        this.game = game;
        this.messageRegistry = messageRegistry;
    }

    @Override
    protected ParseResult<ServerPlayer> parse(Invocation<CommandCause> invocation, Argument<ServerPlayer> context, String argument) {
        if (!game.isServerAvailable()) {
            return ParseResult.failure(messageRegistry.get(LiteSpongeMessages.SERVER_UNAVAILABLE, invocation));
        }

        return game.server().player(argument)
            .map(player -> ParseResult.success(player))
            .orElseGet(() -> ParseResult.failure(messageRegistry.get(LiteSpongeMessages.PLAYER_NOT_FOUND, invocation, argument)));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandCause> invocation, Argument<ServerPlayer> argument, SuggestionContext context) {
        if (!game.isServerAvailable()) {
            return SuggestionResult.empty();
        }

        return game.server().onlinePlayers().stream()
            .collect(SuggestionResult.collector(player -> player.name(), player -> player.uniqueId().toString()));
    }
}
