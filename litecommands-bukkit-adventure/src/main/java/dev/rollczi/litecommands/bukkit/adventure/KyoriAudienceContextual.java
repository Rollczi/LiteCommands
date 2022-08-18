package dev.rollczi.litecommands.bukkit.adventure;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import panda.std.Result;

class KyoriAudienceContextual implements Contextual<CommandSender, Audience> {

    private final KyoriAudienceProvider kyoriAudienceProvider;

    KyoriAudienceContextual(KyoriAudienceProvider kyoriAudienceProvider) {
        this.kyoriAudienceProvider = kyoriAudienceProvider;
    }

    @Override
    public Result<Audience, Object> extract(CommandSender commandSender, Invocation<CommandSender> invocation) {
        return Result.supplyThrowing(IllegalArgumentException.class, () -> this.kyoriAudienceProvider.sender(commandSender))
                .mapErr(Throwable::getMessage);
    }

}
