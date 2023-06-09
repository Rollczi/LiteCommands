package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.result.ResultHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import net.kyori.adventure.text.Component;

class AdventureComponentHandler<SENDER> implements ResultHandler<SENDER, Component> {

    private final AdventureAudienceProvider<SENDER> adventureAudienceProvider;

    AdventureComponentHandler(AdventureAudienceProvider<SENDER> adventureAudienceProvider) {
        this.adventureAudienceProvider = adventureAudienceProvider;
    }

    @Override
    public void handle(Invocation<SENDER> invocation, Component result) {
        this.adventureAudienceProvider.sender(invocation.sender()).sendMessage(result);
    }

}
