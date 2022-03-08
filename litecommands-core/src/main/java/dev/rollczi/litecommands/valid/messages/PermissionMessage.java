package dev.rollczi.litecommands.valid.messages;

import dev.rollczi.litecommands.LiteInvocation;

import java.util.List;

@FunctionalInterface
public interface PermissionMessage extends LiteMessage {

    @Override
    default String message(MessageInfoContext messageInfoContext) {
        return message(messageInfoContext.getInvocation(), messageInfoContext.getMissingPermissions());
    }

    String message(LiteInvocation invocation, List<String> permissions);

}
