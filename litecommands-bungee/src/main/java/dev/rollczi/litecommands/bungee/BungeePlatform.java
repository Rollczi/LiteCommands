package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.meta.CommandMeta;
import dev.rollczi.litecommands.modern.permission.MissingPermissions;
import dev.rollczi.litecommands.modern.platform.AbstractPlatform;
import dev.rollczi.litecommands.modern.platform.PlatformInvocationHook;
import dev.rollczi.litecommands.modern.platform.PlatformSuggestionHook;
import dev.rollczi.litecommands.modern.suggestion.Suggestion;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class BungeePlatform extends AbstractPlatform<CommandSender, LiteBungeeSettings> {

    private final Plugin plugin;
    private final PluginManager pluginManager;

    private final Map<String, BungeeCommand> commands = new HashMap<>();

    public BungeePlatform(Plugin plugin, LiteBungeeSettings liteBungeeSettings) {
        super(liteBungeeSettings);
        this.plugin = plugin;
        this.pluginManager = plugin.getProxy().getPluginManager();
    }

    @Override
    public void register(CommandRoute<CommandSender> commandRoute, PlatformInvocationHook<CommandSender> invocationHook, PlatformSuggestionHook<CommandSender> suggestionHook) {
        for (String name : commandRoute.getAllNames()) {
            BungeeCommand command = new BungeeCommand(commandRoute, name, invocationHook, suggestionHook);

            pluginManager.registerCommand(this.plugin, command);
            commands.put(name, command);
        }
    }

    @Override
    public void unregister(CommandRoute<CommandSender> commandRoute) {
        for (String name : commandRoute.getAllNames()) {
            BungeeCommand bungeeCommand = commands.get(name);

            if (bungeeCommand != null) {
                pluginManager.unregisterCommand(bungeeCommand);
            }
        }
    }

    @Override
    public void unregisterAll() {
        for (BungeeCommand command : commands.values()) {
            pluginManager.unregisterCommand(command);
        }
    }

    private class BungeeCommand extends Command implements TabExecutor {

        private final CommandRoute<CommandSender> commandSection;
        private final String label;
        private final PlatformInvocationHook<CommandSender> executeListener;
        private final PlatformSuggestionHook<CommandSender> suggestionListener;

        public BungeeCommand(CommandRoute<CommandSender> command, String label, PlatformInvocationHook<CommandSender> executeListener, PlatformSuggestionHook<CommandSender> suggestionListener) {
            super(command.getName(), "", command.getAliases().toArray(new String[0]));
            this.commandSection = command;
            this.label = label;
            this.executeListener = executeListener;
            this.suggestionListener = suggestionListener;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            this.executeListener.execute(this.newInvocation(sender, args));
        }

        @Override
        public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
            return this.suggestionListener.suggest(this.newInvocation(sender, args))
                .getSuggestions()
                .stream()
                .map(Suggestion::multilevel)
                .collect(Collectors.toList());
        }

        private Invocation<CommandSender> newInvocation(CommandSender sender, String[] args) {
            return new Invocation<>(sender, new BungeeSender(sender), this.commandSection.getName(), this.label, args);
        }

        @Override
        public boolean hasPermission(CommandSender sender) {
            boolean isNative = commandSection.getMeta().get(CommandMeta.NATIVE_PERMISSIONS);

            if (isNative || liteConfiguration.isNativePermissions()) {
                MissingPermissions missingPermissions = MissingPermissions.check(new BungeeSender(sender), this.commandSection);

                return missingPermissions.isPermitted();
            }

            return super.hasPermission(sender);
        }
    }
}
