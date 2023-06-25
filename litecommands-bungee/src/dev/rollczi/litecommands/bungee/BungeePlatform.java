package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class BungeePlatform extends AbstractPlatform<CommandSender, LiteBungeeSettings> {

    private final Plugin plugin;
    private final PluginManager pluginManager;

    public BungeePlatform(Plugin plugin, LiteBungeeSettings liteBungeeSettings) {
        super(liteBungeeSettings);
        this.plugin = plugin;
        this.pluginManager = plugin.getProxy().getPluginManager();
    }

    @Override
    protected void hook(CommandRoute<CommandSender> commandRoute, PlatformInvocationListener<CommandSender> invocationHook, PlatformSuggestionListener<CommandSender> suggestionHook) {
        for (String label : commandRoute.names()) {
            BungeeCommand command = new BungeeCommand(settings, commandRoute, label, invocationHook, suggestionHook);

            pluginManager.registerCommand(this.plugin, command);
        }
    }

    @Override
    protected void unhook(CommandRoute<CommandSender> commandRoute) {
        List<Command> commands = pluginManager.getCommands().stream()
            .map(Map.Entry::getValue)
            .filter(command -> command instanceof BungeeCommand)
            .filter(command -> commandRoute.isNameOrAlias(command.getName()))
            .collect(Collectors.toList());

        for (Command command : commands) {
            pluginManager.unregisterCommand(command);
        }
    }

}
