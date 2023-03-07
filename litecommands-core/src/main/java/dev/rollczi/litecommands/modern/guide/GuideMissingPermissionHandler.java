package dev.rollczi.litecommands.modern.guide;

import dev.rollczi.litecommands.modern.command.CommandExecuteResultHandler;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.permission.MissingPermissions;
import dev.rollczi.litecommands.modern.platform.PlatformSender;

public class GuideMissingPermissionHandler<SENDER> implements CommandExecuteResultHandler<SENDER, MissingPermissions> {

    @Override
    public void handle(Invocation<SENDER> invocation, MissingPermissions result) {
        PlatformSender sender = invocation.getPlatformSender();
        String permissions = result.joinPermissions();
        String message = String.format("You don't have permission to execute this command! (%s)", permissions);

        sender.sendMessage(message);
        sender.sendMessage("You can change this message by implementing MissingPermissionsHandler");
        sender.sendMessage("See https://docs.rollczi.dev/litecommands/???"); // TODO add link
    }

}
