package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.invocation.Invocation;
import net.kyori.adventure.audience.Audience;
import panda.std.Result;

class AdventureAudienceContextual<SENDER> implements ContextProvider<SENDER, Audience> {

    private final AdventureAudienceProvider<SENDER> adventureAudienceProvider;

    AdventureAudienceContextual(AdventureAudienceProvider<SENDER> adventureAudienceProvider) {
        this.adventureAudienceProvider = adventureAudienceProvider;
    }

    @Override
    public Result<Audience, Object> provide(Invocation<SENDER> invocation) {
        return Result.supplyThrowing(IllegalArgumentException.class, () -> this.adventureAudienceProvider.sender(invocation.sender()))
            .mapErr(Throwable::getMessage);
    }

}
