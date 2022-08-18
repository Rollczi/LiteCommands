package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;

public interface PermissionHandler<SENDER> extends Handler<SENDER, RequiredPermissions> {

    void handle(SENDER sender, LiteInvocation invocation, RequiredPermissions requiredPermissions);

}
