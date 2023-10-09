package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

class StringHandler<SENDER> implements ResultHandler<SENDER, String> {

    private final AdventureAudienceProvider<SENDER> adventureAudienceProvider;
    private final ComponentSerializer<Component, ?, String> kyoriComponentSerializer;

    StringHandler(AdventureAudienceProvider<SENDER> adventureAudienceProvider, ComponentSerializer<Component, ?, String> kyoriComponentSerializer) {
        this.adventureAudienceProvider = adventureAudienceProvider;
        this.kyoriComponentSerializer = kyoriComponentSerializer;
    }

    @Override
    public void handle(Invocation<SENDER> invocation, String result, ResultHandlerChain<SENDER> chain) {
        this.adventureAudienceProvider.sender(invocation).sendMessage(this.kyoriComponentSerializer.deserialize(result));
    }

}
