package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.argument.suggestion.input.SuggestionInput;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

class BukkitCommand extends org.bukkit.command.Command {

    private final LiteBukkitSettings settings;
    private final CommandRoute<CommandSender> commandRoute;
    private final PlatformInvocationListener<CommandSender> invocationHook;
    private final PlatformSuggestionListener<CommandSender> suggestionHook;

    BukkitCommand(LiteBukkitSettings settings, CommandRoute<CommandSender> commandRoute, PlatformInvocationListener<CommandSender> invocationHook, PlatformSuggestionListener<CommandSender> suggestionHook) {
        super(commandRoute.getName(), "", "/" + commandRoute.getName(), commandRoute.getAliases());
        this.settings = settings;
        this.commandRoute = commandRoute;
        this.invocationHook = invocationHook;
        this.suggestionHook = suggestionHook;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        ParseableInput<?> input = ParseableInput.raw(args);
        PlatformSender platformSender = new BukkitSender(sender);

        this.invocationHook.execute(new Invocation<>(sender, platformSender, commandRoute.getName(), alias, input), input);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        SuggestionInput<?> input = SuggestionInput.raw(args);
        PlatformSender platformSender = new BukkitSender(sender);

        return this.suggestionHook.suggest(new Invocation<>(sender, platformSender, commandRoute.getName(), alias, input), input)
            .asMultiLevelList();
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
