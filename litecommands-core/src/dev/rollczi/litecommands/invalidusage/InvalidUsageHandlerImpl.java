package dev.rollczi.litecommands.invalidusage;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.message.MessageRegistry;

public class InvalidUsageHandlerImpl<SENDER> implements InvalidUsageHandler<SENDER> {

    private final MessageRegistry messageRegistry;

    public InvalidUsageHandlerImpl(MessageRegistry messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public void handle(Invocation<SENDER> invocation, InvalidUsage<SENDER> result, ResultHandlerChain<SENDER> chain) {
        this.messageRegistry.get(LiteMessages.INVALID_USAGE, result)
            .ifPresent(object -> chain.resolve(invocation, object));
    }

}
