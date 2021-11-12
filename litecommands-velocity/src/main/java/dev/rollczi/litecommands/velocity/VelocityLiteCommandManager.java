package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.LiteCommandManager;
import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.component.ScopeMetaData;

import java.util.HashSet;
import java.util.Set;

public class VelocityLiteCommandManager implements LiteCommandManager {

    private final Set<String> commands = new HashSet<>();
    private final ProxyServer proxyServer;

    public VelocityLiteCommandManager(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void registerCommand(ScopeMetaData scope, CommandInvocationExecutor function) {
        SimpleCommand command = invocation -> function.execute(new LiteInvocation(scope.getName(), invocation.alias(), new VelocitySender(invocation.source()), invocation.arguments()));

        CommandManager commandManager = proxyServer.getCommandManager();
        CommandMeta meta = commandManager.metaBuilder(scope.getName())
                .aliases(scope.getAliases().toArray(new String[0]))
                .build();

        commandManager.register(meta, command);
        commands.add(scope.getName());
        commands.addAll(scope.getAliases());
    }

    public void unregisterCommands() {
        for (String command : commands) {
            proxyServer.getCommandManager().unregister(command);
        }
    }

}
