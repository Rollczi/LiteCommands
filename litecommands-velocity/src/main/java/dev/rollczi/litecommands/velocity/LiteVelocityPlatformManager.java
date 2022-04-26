package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.platform.LiteAbstractPlatformManager;
import dev.rollczi.litecommands.scope.ScopeMetaData;
import dev.rollczi.litecommands.platform.Executor;
import dev.rollczi.litecommands.platform.Suggester;

import java.util.HashSet;
import java.util.Set;

public class LiteVelocityPlatformManager extends LiteAbstractPlatformManager<CommandSource> {

    private final Set<String> commands = new HashSet<>();
    private final ProxyServer proxyServer;

    public LiteVelocityPlatformManager(ProxyServer proxyServer) {
        super(LiteVelocitySender::new);
        this.proxyServer = proxyServer;
    }

    @Override
    public void registerCommand(ScopeMetaData scope, Executor executor, Suggester suggester) {
        LiteVelocityCommand command = new LiteVelocityCommand(scope, executor, suggester, this.liteSenderCreator);

        CommandManager commandManager = this.proxyServer.getCommandManager();
        CommandMeta meta = commandManager.metaBuilder(scope.getName())
                .aliases(scope.getAliases().toArray(new String[0]))
                .build();

        commandManager.register(meta, command);
        this.commands.add(scope.getName());
        this.commands.addAll(scope.getAliases());
    }

    @Override
    public void unregisterCommands() {
        for (String command : this.commands) {
            this.proxyServer.getCommandManager().unregister(command);
        }
    }

}
