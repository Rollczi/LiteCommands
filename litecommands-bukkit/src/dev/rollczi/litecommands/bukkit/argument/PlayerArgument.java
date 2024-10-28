package dev.rollczi.litecommands.bukkit.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class PlayerArgument extends ArgumentResolver<CommandSender, Player> {

    private final Server server;
    private final MessageRegistry<CommandSender> messageRegistry;

    public PlayerArgument(Server server, MessageRegistry<CommandSender> messageRegistry) {
        this.server = server;
        this.messageRegistry = messageRegistry;
    }

    @Override
    protected ParseResult<Player> parse(Invocation<CommandSender> invocation, Argument<Player> context, String argument) {
        Player player = server.getPlayer(argument);

        if (player != null) {
            return ParseResult.success(player);
        }

        return ParseResult.failure(messageRegistry.getInvoked(LiteBukkitMessages.PLAYER_NOT_FOUND, invocation, argument));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Player> argument, SuggestionContext context) {
        return SuggestionResult.from(
            server.getOnlinePlayers().stream()
                .map(player -> Suggestion.of(player.getName(), player.getUniqueId().toString()))
                .collect(Collectors.toList())
        );
    }

}
