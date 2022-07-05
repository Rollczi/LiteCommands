package dev.rollczi.litecommands.bukkit.adventure;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import panda.std.Result;

class KyoriAudienceContextual implements Contextual<CommandSender, Audience> {

    private final KyoriAudienceExtractor kyoriAudienceExtractor;

    KyoriAudienceContextual(KyoriAudienceExtractor kyoriAudienceExtractor) {
        this.kyoriAudienceExtractor = kyoriAudienceExtractor;
    }

    @Override
    public Result<Audience, Object> extract(CommandSender commandSender, Invocation<CommandSender> invocation) {
        return Result.attempt(IllegalArgumentException.class, () -> this.kyoriAudienceExtractor.extract(commandSender))
                .mapErr(Throwable::getMessage);
    }

}
