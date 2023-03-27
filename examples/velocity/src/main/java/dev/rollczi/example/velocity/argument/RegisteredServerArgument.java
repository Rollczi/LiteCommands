package dev.rollczi.example.velocity.argument;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import panda.std.Option;
import panda.std.Result;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ArgumentName("server")
public class RegisteredServerArgument implements OneArgument<RegisteredServer> {

    private final ProxyServer server;

    public RegisteredServerArgument(ProxyServer server) {
        this.server = server;
    }

    @Override
    public Result<RegisteredServer, ?> parse(LiteInvocation liteInvocation, String argument) {
        Optional<RegisteredServer> registeredServer = this.server.getServer(argument);

        if (registeredServer.isPresent()) {
            return Result.ok(registeredServer.get());
        }

        return Result.error("Server not found");
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return server.getAllServers().stream()
            .map(registeredServer -> registeredServer.getServerInfo())
            .map(serverInfo -> serverInfo.getName())
            .map(rawSuggestion -> Suggestion.of(rawSuggestion))
            .collect(Collectors.toList());
    }

}
