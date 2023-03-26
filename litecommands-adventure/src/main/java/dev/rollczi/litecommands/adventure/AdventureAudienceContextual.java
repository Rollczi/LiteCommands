package dev.rollczi.litecommands.adventure;

import dev.rollczi.litecommands.bind.BindContextual;
import dev.rollczi.litecommands.invocation.Invocation;
import net.kyori.adventure.audience.Audience;
import panda.std.Result;

class AdventureAudienceContextual<SENDER> implements BindContextual<SENDER, Audience> {

    private final AdventureAudienceProvider<SENDER> adventureAudienceProvider;

    AdventureAudienceContextual(AdventureAudienceProvider<SENDER> adventureAudienceProvider) {
        this.adventureAudienceProvider = adventureAudienceProvider;
    }

    @Override
    public Result<Audience, Object> extract(Invocation<SENDER> invocation) {
        return Result.supplyThrowing(IllegalArgumentException.class, () -> this.adventureAudienceProvider.sender(invocation.getSender()))
            .mapErr(Throwable::getMessage);
    }

}
