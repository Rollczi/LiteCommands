package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.RegistryPlatform;
import dev.rollczi.litecommands.platform.SuggestionListener;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class LiteBungeeRegistryPlatform implements RegistryPlatform<CommandSender> {

    private final Set<String> commands = new HashSet<>();
    private final Plugin plugin;
    private final boolean nativePermissions;

    public LiteBungeeRegistryPlatform(Plugin plugin, boolean nativePermissions) {
        this.plugin = plugin;
        this.nativePermissions = nativePermissions;
    }

    @Override
    public void registerListener(CommandSection<CommandSender> section, ExecuteListener<CommandSender> executeListener, SuggestionListener<CommandSender> suggestionListener) {
        BungeeCommand command = new BungeeCommand(section, executeListener, suggestionListener, nativePermissions);

        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, command);
        this.commands.add(section.getName());
        this.commands.addAll(section.getAliases());
    }

    @Override
    public void unregisterListener(CommandSection<CommandSender> command) {
        PluginManager pluginManager = this.plugin.getProxy().getPluginManager();

        new ArrayList<>(pluginManager.getCommands()).stream()
                .filter(entry -> command.getName().equals(entry.getKey()) || command.getAliases().contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(pluginManager::unregisterCommand);
    }

    @Override
    public void unregisterAll() {
        PluginManager pluginManager = this.plugin.getProxy().getPluginManager();

        new ArrayList<>(pluginManager.getCommands()).stream()
                .filter(entry -> this.commands.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(pluginManager::unregisterCommand);
    }
}
