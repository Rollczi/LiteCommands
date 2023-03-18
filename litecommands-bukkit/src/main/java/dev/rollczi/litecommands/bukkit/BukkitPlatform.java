package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.permission.MissingPermissions;
import dev.rollczi.litecommands.modern.platform.Platform;
import dev.rollczi.litecommands.modern.platform.PlatformInvocationHook;
import dev.rollczi.litecommands.modern.platform.PlatformSuggestionHook;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class BukkitPlatform implements Platform<CommandSender, LiteBukkitConfiguration> {

    private final Set<String> commands = new HashSet<>();

    private final CommandMap commandMap;
    private final Map<String, org.bukkit.command.Command> knownCommands;

    private LiteBukkitConfiguration liteBukkitConfiguration;

    @SuppressWarnings("unchecked")
    BukkitPlatform(Server server, LiteBukkitConfiguration liteBukkitConfiguration) {
        this.liteBukkitConfiguration = liteBukkitConfiguration;

        try {
            Field commandMapField = server.getClass().getDeclaredField("commandMap");

            commandMapField.setAccessible(true);
            this.commandMap = (CommandMap) commandMapField.get(server);

            Field knownCommandMapField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandMapField.setAccessible(true);

            this.knownCommands = (Map<String, org.bukkit.command.Command>) knownCommandMapField.get(this.commandMap);

        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void setConfiguration(@NotNull LiteBukkitConfiguration liteConfiguration) {
        this.liteBukkitConfiguration = liteConfiguration;
    }

    @Override
    public @NotNull LiteBukkitConfiguration getConfiguration() {
        return this.liteBukkitConfiguration;
    }

    @Override
    public void register(CommandRoute<CommandSender> commandRoute, PlatformInvocationHook<CommandSender> invocationHook, PlatformSuggestionHook<CommandSender> suggestionHook) {
        BukkitCommand bukkitSimpleCommand = new BukkitCommand(commandRoute, invocationHook, suggestionHook);

        this.commandMap.register(commandRoute.getName(), this.liteBukkitConfiguration.getFallbackPrefix(), bukkitSimpleCommand);
        this.commands.add(commandRoute.getName());
        this.commands.addAll(commandRoute.getAliases());
    }

    @Override
    public void unregister(CommandRoute<CommandSender> commandRoute) {
        for (String name : commandRoute.getAllNames()) {
            this.knownCommands.remove(name);
            this.commands.remove(name);
        }
    }

    @Override
    public void unregisterAll() {
        for (String command : this.commands) {
            this.knownCommands.remove(command);
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
            return new Invocation<>(sender, new BukkitSender(sender), commandRoute.getName(), alias, args);
        }

        @Override
        public boolean testPermissionSilent(@NotNull CommandSender target) {
            if (!liteBukkitConfiguration.isNativePermission()) {
                return super.testPermissionSilent(target);
            }

            MissingPermissions check = MissingPermissions.check(new BukkitSender(target), commandRoute);

            return check.isPermitted();
        }
    }

}
