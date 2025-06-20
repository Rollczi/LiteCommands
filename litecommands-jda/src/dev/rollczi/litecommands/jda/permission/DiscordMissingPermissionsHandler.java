package dev.rollczi.litecommands.jda.permission;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.jda.LiteJDAMessages;
import dev.rollczi.litecommands.message.MessageRegistry;

public class DiscordMissingPermissionsHandler<SENDER> implements ResultHandler<SENDER, DiscordMissingPermissions> {

    private final MessageRegistry<SENDER> messageRegistry;

    public DiscordMissingPermissionsHandler(MessageRegistry<SENDER> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public void handle(Invocation<SENDER> invocation, DiscordMissingPermissions result, ResultHandlerChain<SENDER> chain) {
        messageRegistry.get(LiteJDAMessages.DISCORD_MISSING_PERMISSIONS, invocation, result)
            .ifPresent(message -> chain.resolve(invocation, message));
    }

}
