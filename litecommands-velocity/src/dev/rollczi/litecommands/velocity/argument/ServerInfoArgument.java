package dev.rollczi.litecommands.velocity.argument;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.velocity.LiteVelocityMessages;

public class ServerInfoArgument extends ArgumentResolver<CommandSource, ServerInfo> {

    private final MessageRegistry<CommandSource> messageRegistry;
    private final ProxyServer server;

    public ServerInfoArgument(MessageRegistry<CommandSource> messageRegistry, ProxyServer server) {
        this.messageRegistry = messageRegistry;
        this.server = server;
    }

    @Override
    protected ParseResult<ServerInfo> parse(Invocation<CommandSource> invocation, Argument<ServerInfo> context, String argument) {
        return this.server.getServer(argument)
            .map(parsed -> ParseResult.success(parsed.getServerInfo()))
            .orElseGet(() -> ParseResult.failure(messageRegistry.get(LiteVelocityMessages.SERVER_NOT_FOUND, invocation, argument)));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSource> invocation, Argument<ServerInfo> argument, SuggestionContext context) {
        return this.server.getAllServers().stream()
            .map(player -> player.getServerInfo())
            .collect(SuggestionResult.collector(server -> server.getName(), server -> server.getAddress().toString()));
    }

}
