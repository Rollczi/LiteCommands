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
import net.md_5.bungee.api.config.ServerInfo;

public class ServerInfoArgument extends ArgumentResolver<CommandSender, ServerInfo> {

    private final MessageRegistry<CommandSender> messageRegistry;
    private final ProxyServer server;

    public ServerInfoArgument(MessageRegistry<CommandSender> messageRegistry, ProxyServer server) {
        this.messageRegistry = messageRegistry;
        this.server = server;
    }

    @Override
    protected ParseResult<ServerInfo> parse(Invocation<CommandSender> invocation, Argument<ServerInfo> context, String argument) {
        ServerInfo serverInfo = this.server.getServerInfo(argument);

        if (serverInfo == null) {
            return ParseResult.failure(messageRegistry.get(LiteBungeeMessages.SERVER_NOT_FOUND, invocation, argument));
        }

        return ParseResult.success(serverInfo);
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<ServerInfo> argument, SuggestionContext context) {
        return this.server.getServers().values().stream()
            .collect(SuggestionResult.collector(server -> server.getName(), server -> server.getAddress().toString()));
    }

}
