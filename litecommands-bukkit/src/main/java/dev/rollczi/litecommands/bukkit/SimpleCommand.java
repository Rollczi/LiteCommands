package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.SuggestionListener;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class SimpleCommand extends org.bukkit.command.Command {

    private static final String NO_PERMISSION_BUKKIT_MESSAGE = ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is a mistake.";
    private static final String PERMISSION_SEPARATOR = ";";

    private final CommandSection<CommandSender> commandSection;
    private final ExecuteListener<CommandSender> executeListener;
    private final SuggestionListener<CommandSender> suggestionListener;
    private final BukkitNoPermission noPermissionHandler;


    SimpleCommand(CommandSection<CommandSender> commandSection, ExecuteListener<CommandSender> executeListener, SuggestionListener<CommandSender> suggestionListener, BukkitNoPermission noPermissionHandler) {
        super(commandSection.getName(), "", "/" + commandSection.getName(), new ArrayList<>(commandSection.getAliases()));
        this.commandSection = commandSection;
        this.executeListener = executeListener;
        this.suggestionListener = suggestionListener;
        this.noPermissionHandler = noPermissionHandler;
        this.initializePermissions();
    }


    private void initializePermissions() {
        CommandMeta commandMeta = this.commandSection.meta();
        Collection<String> permissions = commandMeta.getPermissions();

        if (!permissions.isEmpty()) {
            this.setPermission(String.join(PERMISSION_SEPARATOR, permissions));
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        this.executeListener.execute(sender, new LiteInvocation(new BukkitSender(sender), this.commandSection.getName(), alias, args));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        return this.suggestionListener.suggest(sender, new LiteInvocation(new BukkitSender(sender), this.commandSection.getName(), alias, args)).multilevelSuggestions();
    }

    @Override
    public boolean testPermissionSilent(@NotNull CommandSender target) {
        RequiredPermissions requiredPermissions = RequiredPermissions.of(this.commandSection.meta(), new BukkitSender(target));

        return requiredPermissions.isEmpty();
    }

    @Override
    public boolean testPermission(@NotNull CommandSender target) {
        RequiredPermissions requiredPermissions = RequiredPermissions.of(this.commandSection.meta(), new BukkitSender(target));

        if (requiredPermissions.isEmpty()) {
            return true;
        }

        boolean handled = this.noPermissionHandler.apply(target, requiredPermissions);

        if (handled) {
            return false;
        }

        target.sendMessage(NO_PERMISSION_BUKKIT_MESSAGE);
        return false;
    }

}
