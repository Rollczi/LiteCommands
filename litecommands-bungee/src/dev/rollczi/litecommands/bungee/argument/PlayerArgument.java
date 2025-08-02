package dev.rollczi.litecommands.bungee.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.bungee.LiteBungeeMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerArgument extends ArgumentResolver<CommandSender, ProxiedPlayer> {

    private final MessageRegistry<CommandSender> messageRegistry;
    private final ProxyServer server;

    public PlayerArgument(MessageRegistry<CommandSender> messageRegistry, ProxyServer server) {
        this.messageRegistry = messageRegistry;
        this.server = server;
    }

    @Override
    protected ParseResult<ProxiedPlayer> parse(Invocation<CommandSender> invocation, Argument<ProxiedPlayer> context, String argument) {
        ProxiedPlayer player = this.server.getPlayer(argument);

        if (player == null) {
            return ParseResult.failure(messageRegistry.get(LiteBungeeMessages.PLAYER_NOT_FOUND, invocation, argument));
        }

        return ParseResult.success(player);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<ProxiedPlayer> argument, SuggestionContext context) {
        return this.server.getPlayers().stream()
            .collect(SuggestionResult.collector(player -> player.getName(), player -> player.getUniqueId().toString()));
    }

}
