package dev.rollczi.litecommands.modern.permission;

import dev.rollczi.litecommands.modern.command.CommandExecuteResultHandler;
import dev.rollczi.litecommands.modern.invocation.Invocation;

public interface MissingPermissionsHandler<SENDER> extends CommandExecuteResultHandler<SENDER, MissingPermissions> {

    @Override
    void handle(Invocation<SENDER> invocation, MissingPermissions missingPermissions);

}
