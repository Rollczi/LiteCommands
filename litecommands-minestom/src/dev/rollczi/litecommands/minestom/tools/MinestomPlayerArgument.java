package dev.rollczi.litecommands.minestom.tools;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.resolver.OneArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.suggestion.SuggestionStream;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.network.ConnectionManager;

public class MinestomPlayerArgument<MESSAGE> extends OneArgumentResolver<CommandSender, Player> {

    private final ConnectionManager connectionManager;
    private final MESSAGE playerNotFoundMessage;

    public MinestomPlayerArgument(ConnectionManager connectionManager, MESSAGE playerNotFoundMessage) {
        this.connectionManager = connectionManager;
        this.playerNotFoundMessage = playerNotFoundMessage;
    }

    @Override
    protected ArgumentResult<Player> parse(Invocation<CommandSender> invocation, Argument<Player> context, String argument) {
        Player player = this.connectionManager.getPlayer(argument);

        if (player == null) {
            return ArgumentResult.failure(this.playerNotFoundMessage);
        }

        return ArgumentResult.success(player);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Player> argument, SuggestionContext context) {
        return SuggestionStream.of(this.connectionManager.getOnlinePlayers())
            .collect(Player::getUsername);
    }

}
