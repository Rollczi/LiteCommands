package dev.rollczi.litecommands.sponge.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.suggestion.SuggestionStream;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class ServerPlayerArgument<MESSAGE> extends ArgumentResolver<CommandCause, ServerPlayer> {

    private final Game game;
    private final MESSAGE serverUnavailableMessage;
    private final MESSAGE playerNotFoundMessage;

    public ServerPlayerArgument(Game game, MESSAGE serverUnavailableMessage, MESSAGE playerNotFoundMessage) {
        this.game = game;
        this.serverUnavailableMessage = serverUnavailableMessage;
        this.playerNotFoundMessage = playerNotFoundMessage;
    }

    @Override
    protected ParseResult<ServerPlayer> parse(Invocation<CommandCause> invocation, Argument<ServerPlayer> context, String argument) {
        if (!game.isServerAvailable()) {
            return ParseResult.failure(serverUnavailableMessage);
        }

        return game.server().player(argument)
            .map(player -> ParseResult.success(player))
            .orElseGet(() -> ParseResult.failure(playerNotFoundMessage));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandCause> invocation, Argument<ServerPlayer> argument, SuggestionContext context) {
        if (!game.isServerAvailable()) {
            return SuggestionResult.empty();
        }

        try (SuggestionStream<ServerPlayer> stream = SuggestionStream.of(game.server().onlinePlayers())) {
            return stream.collect(player -> player.name());
        }
    }
}
