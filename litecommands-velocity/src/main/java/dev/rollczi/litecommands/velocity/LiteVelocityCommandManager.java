package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.LiteCommandManager;
import dev.rollczi.litecommands.component.ScopeMetaData;

import java.util.HashSet;
import java.util.Set;

public class LiteVelocityCommandManager implements LiteCommandManager {

    private final Set<String> commands = new HashSet<>();
    private final ProxyServer proxyServer;

    public LiteVelocityCommandManager(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void registerCommand(ScopeMetaData scope, Executor executor, Suggester suggester) {
        LiteVelocityCommand command = new LiteVelocityCommand(scope, executor, suggester);

        CommandManager commandManager = proxyServer.getCommandManager();
        CommandMeta meta = commandManager.metaBuilder(scope.getName())
                .aliases(scope.getAliases().toArray(new String[0]))
                .build();

        commandManager.register(meta, command);
        commands.add(scope.getName());
        commands.addAll(scope.getAliases());
    }

    @Override
    public void unregisterCommands() {
        for (String command : commands) {
            proxyServer.getCommandManager().unregister(command);
        }
    }

}
