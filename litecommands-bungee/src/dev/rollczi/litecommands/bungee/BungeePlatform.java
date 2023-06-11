package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.argument.input.InputArguments;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.suggestion.Suggestion;
import dev.rollczi.litecommands.suggestion.input.SuggestionInput;
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
    protected void hook(CommandRoute<CommandSender> commandRoute, PlatformInvocationListener<CommandSender> invocationHook, PlatformSuggestionListener<CommandSender> suggestionHook) {
        for (String name : commandRoute.names()) {
            BungeeCommand command = new BungeeCommand(commandRoute, name, invocationHook, suggestionHook);

            pluginManager.registerCommand(this.plugin, command);
            commands.put(name, command);
        }
    }

    @Override
    protected void unhook(CommandRoute<CommandSender> commandRoute) {
        for (String name : commandRoute.names()) {
            BungeeCommand bungeeCommand = commands.get(name);

            if (bungeeCommand != null) {
                pluginManager.unregisterCommand(bungeeCommand);
            }

            commands.remove(name);
        }
    }

    private class BungeeCommand extends Command implements TabExecutor {

        private final CommandRoute<CommandSender> commandSection;
        private final String label;
        private final PlatformInvocationListener<CommandSender> executeListener;
        private final PlatformSuggestionListener<CommandSender> suggestionListener;

        public BungeeCommand(CommandRoute<CommandSender> command, String label, PlatformInvocationListener<CommandSender> executeListener, PlatformSuggestionListener<CommandSender> suggestionListener) {
            super(command.getName(), "", command.getAliases().toArray(new String[0]));
            this.commandSection = command;
            this.label = label;
            this.executeListener = executeListener;
            this.suggestionListener = suggestionListener;
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            this.executeListener.execute(this.newInvocation(sender, args), InputArguments.raw(args));
        }

        @Override
        public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
            return this.suggestionListener.suggest(this.newInvocation(sender, args), SuggestionInput.raw(args))
                .asMultiLevelList();
        }

        private Invocation<CommandSender> newInvocation(CommandSender sender, String[] args) {
            return new Invocation<>(sender, new BungeeSender(sender), this.commandSection.getName(), this.label, InputArguments.raw(args));
        }

        @Override
        public boolean hasPermission(CommandSender sender) {
            boolean isNative = commandSection.meta().get(CommandMeta.NATIVE_PERMISSIONS);

            if (isNative || liteConfiguration.isNativePermissions()) {
                MissingPermissions missingPermissions = MissingPermissions.check(new BungeeSender(sender), this.commandSection);

                return missingPermissions.isPermitted();
            }

            return super.hasPermission(sender);
        }

    }

}
