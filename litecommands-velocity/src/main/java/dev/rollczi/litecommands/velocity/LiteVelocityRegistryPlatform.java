package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.platform.Suggester;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.RegistryPlatform;

import java.util.HashSet;
import java.util.Set;

class LiteVelocityRegistryPlatform implements RegistryPlatform<CommandSource> {

    private final Set<String> commands = new HashSet<>();
    private final ProxyServer proxyServer;

    public LiteVelocityRegistryPlatform(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void registerListener(CommandSection command, ExecuteListener<CommandSource> listener, Suggester<CommandSource> suggester) {
        VelocityCommand velocityCommand = new VelocityCommand(command.getName(), listener, suggester);

        CommandManager commandManager = this.proxyServer.getCommandManager();
        CommandMeta meta = commandManager.metaBuilder(command.getName())
                .aliases(command.getAliases().toArray(new String[0]))
                .build();

        commandManager.register(meta, velocityCommand);

        this.commands.add(command.getName());
        this.commands.addAll(command.getAliases());
    }

    @Override
    public void unregisterListener(CommandSection command) {
        this.proxyServer.getCommandManager().unregister(command.getName());

        for (String alias : command.getAliases()) {
            this.proxyServer.getCommandManager().unregister(alias);
        }
    }

    @Override
    public void unregisterAll() {
        for (String command : this.commands) {
            this.proxyServer.getCommandManager().unregister(command);
        }
    }

}
