package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.platform.Suggester;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.RegistryPlatform;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class LiteBungeeRegistryPlatform implements RegistryPlatform<CommandSender> {

    private final Set<String> commands = new HashSet<>();
    private final Plugin plugin;
    
    public LiteBungeeRegistryPlatform(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void registerListener(CommandSection section, ExecuteListener<CommandSender> executeListener, Suggester<CommandSender> suggester) {
        BungeeCommand command = new BungeeCommand(section, executeListener, suggester);

        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, command);
        this.commands.add(section.getName());
        this.commands.addAll(section.getAliases());
    }

    @Override
    public void unregisterListener(CommandSection command) {
        PluginManager pluginManager = this.plugin.getProxy().getPluginManager();

        pluginManager.getCommands().stream()
                .filter(entry -> command.getName().equals(entry.getKey()) || command.getAliases().contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(pluginManager::unregisterCommand);
    }

    @Override
    public void unregisterAll() {
        PluginManager pluginManager = this.plugin.getProxy().getPluginManager();

        pluginManager.getCommands().stream()
                .filter(entry -> this.commands.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(pluginManager::unregisterCommand);
    }
}
