package dev.rollczi.litecommands.cooldown;

import static dev.rollczi.litecommands.message.LiteMessages.COMMAND_COOLDOWN;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;

public class CooldownStateResultHandler<SENDER> implements ResultHandler<SENDER, CooldownState> {

    private final MessageRegistry<SENDER> messageRegistry;

    public CooldownStateResultHandler(MessageRegistry<SENDER> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public void handle(Invocation<SENDER> invocation, CooldownState cooldownState, ResultHandlerChain<SENDER> chain) {
        chain.resolve(invocation, this.messageRegistry.get(COMMAND_COOLDOWN, invocation, cooldownState));
    }

}

