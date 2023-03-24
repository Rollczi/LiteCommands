package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.command.CommandExecuteResultHandler;
import dev.rollczi.litecommands.invocation.Invocation;

public interface MissingPermissionsHandler<SENDER> extends CommandExecuteResultHandler<SENDER, MissingPermissions> {

    @Override
    void handle(Invocation<SENDER> invocation, MissingPermissions missingPermissions);

}
