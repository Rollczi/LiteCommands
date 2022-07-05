package dev.rollczi.litecommands.bukkit.adventure;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.std.Option;
import panda.std.Result;

public class KyoriAudienceContextual implements Contextual<CommandSender, Audience> {

    private final AudienceProvider audienceProvider;

    public KyoriAudienceContextual(AudienceProvider audienceProvider) {
        this.audienceProvider = audienceProvider;
    }

    @Override
    public Result<Audience, Object> extract(CommandSender commandSource, Invocation<CommandSender> invocation) {
        return Option.of(commandSource)
                .is(Player.class)
                .map(player -> this.audienceProvider.player(player.getUniqueId()))
                .orElse(this.audienceProvider.console())
                .toResult("Couldn't find the audience for that command source.");
    }
}
