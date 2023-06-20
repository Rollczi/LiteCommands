package dev.rollczi.example.velocity.argument;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.argument.suggestion.SuggestionContext;
import dev.rollczi.litecommands.argument.suggestion.SuggestionResult;
import dev.rollczi.litecommands.argument.suggestion.SuggestionStream;

import java.util.Optional;

public class RegisteredServerArgument extends ArgumentResolver<CommandSource, RegisteredServer> {

    private final ProxyServer server;

    public RegisteredServerArgument(ProxyServer server) {
        this.server = server;
    }

    @Override
    protected ParseResult<RegisteredServer> parse(Invocation<CommandSource> invocation, Argument<RegisteredServer> context, String argument) {
        Optional<RegisteredServer> optionalServer = server.getServer(argument);

        return optionalServer.map(server -> ParseResult.success(server))
            .orElseGet(() -> ParseResult.failure("Server " + argument + " not found"));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSource> invocation, Argument<RegisteredServer> argument, SuggestionContext context) {
        return SuggestionStream.of(server.getAllServers())
            .map(registeredServer -> registeredServer.getServerInfo())
            .collect(serverInfo -> serverInfo.getName());
    }

}
