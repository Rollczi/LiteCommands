package dev.rollczi.litecommands.permission;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;

import static dev.rollczi.litecommands.message.LiteMessages.MISSING_PERMISSIONS;

public class MissingPermissionResultHandler<SENDER> implements ResultHandler<SENDER, MissingPermissions> {

    private final MessageRegistry<SENDER> messageRegistry;

    public MissingPermissionResultHandler(MessageRegistry<SENDER> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public void handle(Invocation<SENDER> invocation, MissingPermissions missingPermissions, ResultHandlerChain<SENDER> chain) {
        this.messageRegistry.getInvoked(MISSING_PERMISSIONS, invocation, missingPermissions)
            .ifPresent(object -> chain.resolve(invocation, object));
    }

}
