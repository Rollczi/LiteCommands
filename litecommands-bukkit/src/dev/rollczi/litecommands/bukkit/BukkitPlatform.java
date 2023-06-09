package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.argument.input.InputArguments;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformInvocationHook;
import dev.rollczi.litecommands.platform.PlatformSuggestionHook;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class BukkitPlatform implements Platform<CommandSender, LiteBukkitSettings> {

    private final Set<String> commands = new HashSet<>();

    private LiteBukkitSettings settings;

    BukkitPlatform(LiteBukkitSettings settings) {
        this.settings = settings;
    }

    @Override
    public void setConfiguration(@NotNull LiteBukkitSettings liteConfiguration) {
        this.settings = liteConfiguration;
    }

    @Override
    public @NotNull LiteBukkitSettings getConfiguration() {
        return this.settings;
    }

    @Override
    public void register(CommandRoute<CommandSender> commandRoute, PlatformInvocationHook<CommandSender> invocationHook, PlatformSuggestionHook<CommandSender> suggestionHook) {
        BukkitCommand bukkitSimpleCommand = new BukkitCommand(commandRoute, invocationHook, suggestionHook);

        this.settings.commandsProvider().commandMap().register(commandRoute.getName(), this.settings.fallbackPrefix(), bukkitSimpleCommand);
        this.commands.add(commandRoute.getName());
        this.commands.addAll(commandRoute.getAliases());
    }

    @Override
    public void unregister(CommandRoute<CommandSender> commandRoute) {
        for (String name : commandRoute.names()) {
            this.settings.commandsProvider().knownCommands().remove(name);
            this.commands.remove(name);
        }
    }

    @Override
    public void unregisterAll() {
        for (String command : this.commands) {
            this.settings.commandsProvider().knownCommands().remove(command);
        }

        this.commands.clear();
    }

    private class BukkitCommand extends org.bukkit.command.Command {

        private final CommandRoute<CommandSender> commandRoute;
        private final PlatformInvocationHook<CommandSender> invocationHook;
        private final PlatformSuggestionHook<CommandSender> suggestionHook;


        BukkitCommand(CommandRoute<CommandSender> commandRoute, PlatformInvocationHook<CommandSender> invocationHook, PlatformSuggestionHook<CommandSender> suggestionHook) {
            super(commandRoute.getName(), "", "/" + commandRoute.getName(), commandRoute.getAliases());
            this.commandRoute = commandRoute;
            this.invocationHook = invocationHook;
            this.suggestionHook = suggestionHook;
        }

        @Override
        public boolean execute(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
            this.invocationHook.execute(this.newInvocation(sender, alias, args));
            return true;
        }

        @Override
        public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
            return this.suggestionHook.suggest(this.newInvocation(sender, alias, args)).asMultiLevelList();
        }

        private Invocation<CommandSender> newInvocation(CommandSender sender, String alias, String[] args) {
            return new Invocation<>(sender, new BukkitSender(sender), commandRoute.getName(), alias, InputArguments.raw(args));
        }

        @Override
        public boolean testPermissionSilent(@NotNull CommandSender target) {
            if (!settings.nativePermission()) {
                return super.testPermissionSilent(target);
            }

            MissingPermissions check = MissingPermissions.check(new BukkitSender(target), commandRoute);

            return check.isPermitted();
        }

    }

}
