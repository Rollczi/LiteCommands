package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.TabCompleteEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.LiteCommandManager;
import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.component.ScopeMetaData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VelocityLiteCommandManager implements LiteCommandManager {

    private final Map<String, TabCompleteInvocation> completeInvocations = new HashMap<>();
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

    @Override
    public void registerTabulation(ScopeMetaData scope, TabCompleteInvocation completeInvocation) {
        completeInvocations.put(scope.getName(), completeInvocation);

        for (String alias : scope.getAliases()) {
            completeInvocations.put(alias, completeInvocation);
        }
    }

    @Subscribe
    public void onTabComplete(TabCompleteEvent event) {
        TabCompleteInvocation tabCompleteInvocation = completeInvocations.get(event.getPartialMessage());

        if (tabCompleteInvocation == null) {
            return;
        }

        tabCompleteInvocation.execute();
    }

    public void unregisterCommands() {
        for (String command : commands) {
            proxyServer.getCommandManager().unregister(command);
        }
    }

    @Override
    public void registerTabulations() {

    }

}
