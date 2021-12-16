package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.LitePlatformManager;
import dev.rollczi.litecommands.component.ScopeMetaData;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LiteBungeePlatformManager implements LitePlatformManager {

    private final Set<String> commands = new HashSet<>();
    private final Plugin plugin;
    
    public LiteBungeePlatformManager(Plugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void registerCommand(ScopeMetaData scope, Executor execute, Suggester suggester) {
        LiteBungeeCommand command = new LiteBungeeCommand(scope, execute, suggester);

        plugin.getProxy().getPluginManager().registerCommand(plugin, command);
        commands.add(scope.getName());
        commands.addAll(scope.getAliases());
    }

    @Override
    public void unregisterCommands() {
        PluginManager pluginManager = plugin.getProxy().getPluginManager();

        Set<Command> registeredCommands = pluginManager.getCommands().stream()
                .filter(entry -> commands.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
        for (Command command : registeredCommands) {
            pluginManager.unregisterCommand(command);
        }
    }
}
