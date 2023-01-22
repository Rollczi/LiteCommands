package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.SuggestionListener;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

class SimpleBlockedCommand extends SimpleCommand {

    private static final String NO_PERMISSION_BUKKIT_MESSAGE = ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is a mistake.";
    private static final String PERMISSION_SEPARATOR = ";";

    private final CommandSection<CommandSender> commandSection;
    private final BukkitNoPermission noPermissionHandler;

    SimpleBlockedCommand(CommandSection<CommandSender> commandSection, ExecuteListener<CommandSender> executeListener, SuggestionListener<CommandSender> suggestionListener, BukkitNoPermission noPermissionHandler) {
        super(commandSection, executeListener, suggestionListener);
        this.commandSection = commandSection;
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
