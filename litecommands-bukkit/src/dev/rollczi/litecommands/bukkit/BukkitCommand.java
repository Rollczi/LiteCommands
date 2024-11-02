package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.argument.suggester.input.SuggestionInput;
import dev.rollczi.litecommands.suggestion.Suggestion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BukkitCommand extends Command {

    private static final Logger LOGGER = Logger.getLogger(BukkitCommand.class.getName());

    private final LiteBukkitSettings settings;
    private final CommandRoute<CommandSender> commandRoute;
    private final PlatformInvocationListener<CommandSender> invocationHook;
    private final PlatformSuggestionListener<CommandSender> suggestionHook;
    private boolean syncTabComplete = false;

    BukkitCommand(LiteBukkitSettings settings, CommandRoute<CommandSender> commandRoute, PlatformInvocationListener<CommandSender> invocationHook, PlatformSuggestionListener<CommandSender> suggestionHook) {
        super(commandRoute.getName(), "", "/" + commandRoute.getName(), commandRoute.getAliases());
        this.settings = settings;
        this.commandRoute = commandRoute;
        this.invocationHook = invocationHook;
        this.suggestionHook = suggestionHook;
    }

    public void setSyncTabComplete(boolean syncTabComplete) {
        this.syncTabComplete = syncTabComplete;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        ParseableInput<?> input = ParseableInput.raw(args);
        PlatformSender platformSender = new BukkitPlatformSender(sender);

        this.invocationHook.execute(new Invocation<>(sender, platformSender, commandRoute.getName(), alias, input), input);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        if (this.syncTabComplete) {
            CompletableFuture<Set<Suggestion>> future = this.suggest(sender, alias, args);

            try {
                return future.get(0, TimeUnit.MILLISECONDS).stream()
                    .map(suggestion -> suggestion.multilevel())
                    .collect(Collectors.toList());
            }
            catch (TimeoutException exception) {
                if (settings.syncSuggestionWarning()) {
                    LOGGER.warning("Asynchronous tab completions are not supported on current server version.");
                    LOGGER.warning("Use server 1.12+ Paper version or install ProtocolLib plugin.");
                }

                return Collections.emptyList();
            }
            catch (InterruptedException | ExecutionException exception) {
                throw new RuntimeException(exception);
            }
        }

        return Collections.emptyList();
    }

    public CompletableFuture<Set<Suggestion>> suggest(CommandSender sender, String alias, String[] args) {
        SuggestionInput<?> input = SuggestionInput.raw(args);
        PlatformSender platformSender = new BukkitPlatformSender(sender);
        Invocation<CommandSender> invocation = new Invocation<>(sender, platformSender, commandRoute.getName(), alias, input);

        return CompletableFuture.completedFuture(this.suggestionHook.suggest(invocation, input).getSuggestions()); // TODO Run suggestion asynchronously inside LiteCommands platform
    }

    @Override
    public boolean testPermissionSilent(@NotNull CommandSender target) {
        if (!settings.nativePermission()) {
            return super.testPermissionSilent(target);
        }

        MissingPermissions check = MissingPermissions.check(new BukkitPlatformSender(target), commandRoute);

        return check.isPermitted();
    }

    public CommandRoute<CommandSender> getCommandRoute() {
        return commandRoute;
    }

}
