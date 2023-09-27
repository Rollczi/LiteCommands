package dev.rollczi.litecommands.bukkit.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerArgument extends ArgumentResolver<CommandSender, Player> {

    private final Server server;
    private final MessageRegistry messageRegistry;

    public PlayerArgument(Server server, MessageRegistry messageRegistry) {
        this.server = server;
        this.messageRegistry = messageRegistry;
    }

    @Override
    protected ParseResult<Player> parse(Invocation<CommandSender> invocation, Argument<Player> context, String argument) {
        Player player = server.getPlayer(argument);

        if (player != null) {
            return ParseResult.success(player);
        }

        return ParseResult.failure(messageRegistry.get(LiteBukkitMessages.PLAYER_NOT_FOUND, argument));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Player> argument, SuggestionContext context) {
        return server.getOnlinePlayers().stream()
            .map(Player::getName)
            .collect(SuggestionResult.collector());
    }

}
