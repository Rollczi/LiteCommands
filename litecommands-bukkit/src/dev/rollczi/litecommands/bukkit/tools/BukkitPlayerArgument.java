package dev.rollczi.litecommands.bukkit.tools;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.resolver.OneArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class BukkitPlayerArgument<MESSAGE> extends OneArgumentResolver<CommandSender, Player> {

    private final Server server;
    private final Function<String, MESSAGE> playerNotFoundMessage;

    public BukkitPlayerArgument(Server server, Function<String, MESSAGE> playerNotFoundMessage) {
        this.server = server;
        this.playerNotFoundMessage = playerNotFoundMessage;
    }

    @Override
    protected ArgumentResult<Player> parse(Invocation<CommandSender> invocation, Argument<Player> context, String argument) {
        Player player = server.getPlayer(argument);

        if (player != null) {
            return ArgumentResult.success(() -> player);
        }

        return ArgumentResult.failure(playerNotFoundMessage);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<Player> argument, SuggestionContext context) {
        return server.getOnlinePlayers().stream()
            .map(Player::getName)
            .collect(SuggestionResult.collector());
    }

}
