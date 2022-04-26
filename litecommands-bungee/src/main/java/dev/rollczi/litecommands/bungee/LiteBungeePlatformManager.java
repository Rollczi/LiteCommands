package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.platform.LiteAbstractPlatformManager;
import dev.rollczi.litecommands.scope.ScopeMetaData;
import dev.rollczi.litecommands.platform.Executor;
import dev.rollczi.litecommands.platform.Suggester;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LiteBungeePlatformManager extends LiteAbstractPlatformManager<CommandSender> {

    private final Set<String> commands = new HashSet<>();
    private final Plugin plugin;
    
    public LiteBungeePlatformManager(Plugin plugin) {
        super(LiteBungeeSender::new);
        this.plugin = plugin;
    }
    
    @Override
    public void registerCommand(ScopeMetaData scope, Executor execute, Suggester suggester) {
        LiteBungeeCommand command = new LiteBungeeCommand(scope, execute, suggester, this.liteSenderCreator);

        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, command);
        this.commands.add(scope.getName());
        this.commands.addAll(scope.getAliases());
    }

    @Override
    public void unregisterCommands() {
        PluginManager pluginManager = this.plugin.getProxy().getPluginManager();
        pluginManager.getCommands().stream()
                .filter(entry -> this.commands.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(pluginManager::unregisterCommand);
    }
}
