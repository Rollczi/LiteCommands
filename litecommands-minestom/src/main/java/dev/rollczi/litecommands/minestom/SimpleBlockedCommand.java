package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.meta.CommandMeta;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.SuggestionListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;

import java.util.Collection;

class SimpleBlockedCommand extends SimpleCommand {

    private static final Component NO_PERMISSION_MINESTOM_MESSAGE = Component.text("I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is a mistake.", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false);

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
                for(String permission : permissions)
                    if(sender.hasPermission(permission))
                        return true;
                sender.sendMessage(NO_PERMISSION_MINESTOM_MESSAGE);
                return false;
            });
        }
    }
}
