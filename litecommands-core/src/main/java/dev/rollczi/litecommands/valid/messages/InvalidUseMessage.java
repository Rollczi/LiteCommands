package dev.rollczi.litecommands.valid.messages;

import dev.rollczi.litecommands.LiteInvocation;

@FunctionalInterface
public interface InvalidUseMessage extends LiteMessage {

    @Override
    default String message(MessageInfoContext messageInfoContext) {
        return message(messageInfoContext.getInvocation(), messageInfoContext.getUseScheme());
    }

    String message(LiteInvocation invocation, String useScheme);

}
