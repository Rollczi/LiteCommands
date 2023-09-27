package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.context.LegacyContextProvider;
import dev.rollczi.litecommands.invocation.Invocation;
import net.kyori.adventure.audience.Audience;
import panda.std.Result;

class AdventureAudienceContextual<SENDER> implements ContextProvider<SENDER, Audience> {

    private final AdventureAudienceProvider<SENDER> adventureAudienceProvider;

    AdventureAudienceContextual(AdventureAudienceProvider<SENDER> adventureAudienceProvider) {
        this.adventureAudienceProvider = adventureAudienceProvider;
    }

    @Override
    public ContextResult<Audience> provide(Invocation<SENDER> invocation) {
        return ContextResult.ok(() -> this.adventureAudienceProvider.sender(invocation));
    }

}
