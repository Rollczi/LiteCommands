package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.command.CommandExecuteResultHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;

class StringHandler<SENDER> implements CommandExecuteResultHandler<SENDER, String> {

    private final AdventureAudienceProvider<SENDER> adventureAudienceProvider;
    private final ComponentSerializer<Component, ?, String> kyoriComponentSerializer;

    StringHandler(AdventureAudienceProvider<SENDER> adventureAudienceProvider, ComponentSerializer<Component, ?, String> kyoriComponentSerializer) {
        this.adventureAudienceProvider = adventureAudienceProvider;
        this.kyoriComponentSerializer = kyoriComponentSerializer;
    }

    @Override
    public void handle(Invocation<SENDER> invocation, String result) {
        this.adventureAudienceProvider.sender(invocation.getSender()).sendMessage(this.kyoriComponentSerializer.deserialize(result));
    }

}
