package dev.rollczi.example.velocity.argument;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.ArgumentResult;
import dev.rollczi.litecommands.argument.resolver.OneArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.suggestion.SuggestionStream;

import java.util.Optional;

public class RegisteredServerArgument extends OneArgumentResolver<CommandSource, RegisteredServer> {

    private final ProxyServer server;

    public RegisteredServerArgument(ProxyServer server) {
        this.server = server;
    }

    @Override
    protected ArgumentResult<RegisteredServer> parse(Invocation<CommandSource> invocation, Argument<RegisteredServer> context, String argument) {
        Optional<RegisteredServer> optionalServer = server.getServer(argument);

        return optionalServer.map(server -> ArgumentResult.success(server))
            .orElseGet(() -> ArgumentResult.failure("Server " + argument + " not found"));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSource> invocation, Argument<RegisteredServer> argument, SuggestionContext suggestion) {
        return SuggestionStream.of(server.getAllServers())
            .map(registeredServer -> registeredServer.getServerInfo())
            .collect(serverInfo -> serverInfo.getName());
    }

}
