package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;

public interface MissingPermissionsHandler<SENDER> extends ResultHandler<SENDER, MissingPermissions> {

    @Override
    void handle(Invocation<SENDER> invocation, MissingPermissions missingPermissions, ResultHandlerChain<SENDER> chain);

}
