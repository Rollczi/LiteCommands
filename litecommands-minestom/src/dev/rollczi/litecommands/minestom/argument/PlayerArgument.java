package dev.rollczi.litecommands.minestom.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.minestom.LiteMinestomMessages;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.network.ConnectionManager;

public class PlayerArgument extends ArgumentResolver<CommandSender, Player> {

    private final ConnectionManager connectionManager;
    private final MessageRegistry<CommandSender> messageRegistry;

    public PlayerArgument(ConnectionManager connectionManager, MessageRegistry<CommandSender> messageRegistry) {
        this.connectionManager = connectionManager;
        this.messageRegistry = messageRegistry;
    }

    @Override
    protected ParseResult<Player> parse(Invocation<CommandSender> invocation, Argument<Player> context, String argument) {
        Player player = this.connectionManager.getOnlinePlayerByUsername(argument);

        if (player == null) {
            return ParseResult.failure(messageRegistry.getInvoked(LiteMinestomMessages.PLAYER_NOT_FOUND, invocation, argument));
        }

        return ParseResult.success(player);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Player> argument, SuggestionContext context) {
        return this.connectionManager.getOnlinePlayers().stream()
            .map(player -> player.getUsername())
            .collect(SuggestionResult.collector());
    }

}
