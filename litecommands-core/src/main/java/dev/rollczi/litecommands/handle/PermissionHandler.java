package dev.rollczi.litecommands.handle;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.LitePermissions;

public interface PermissionHandler<SENDER> extends Handler<SENDER, LitePermissions> {

    void handle(SENDER sender, LiteInvocation invocation, LitePermissions litePermissions);

}
