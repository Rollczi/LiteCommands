package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.SuggestionListener;
import net.minestom.server.command.CommandSender;

import java.util.Collection;

class SimpleBlockedCommand extends SimpleCommand {

    private final CommandSection<CommandSender> commandSection;
    private final MinestomNoPermission noPermissionHandler;

    SimpleBlockedCommand(CommandSection<CommandSender> commandSection, ExecuteListener<CommandSender> executeListener, SuggestionListener<CommandSender> suggestionListener, MinestomNoPermission noPermissionHandler) {
        super(commandSection, executeListener, suggestionListener);
        this.commandSection = commandSection;
        this.noPermissionHandler = noPermissionHandler;
        this.initializePermissions();
    }

    private void initializePermissions() {
        CommandMeta commandMeta = this.commandSection.meta();
        Collection<String> permissions = commandMeta.getPermissions();

        if (!permissions.isEmpty()) {
            setCondition((sender, commandString) -> {
                RequiredPermissions requiredPermissions = RequiredPermissions.of(this.commandSection.meta(), new MinestomSender(sender));

                if (requiredPermissions.isEmpty()) {
                    return true;
                }

                this.noPermissionHandler.apply(sender, requiredPermissions);

                return false;
            });
        }
    }
}
