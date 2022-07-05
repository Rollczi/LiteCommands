package dev.rollczi.litecommands.bukkit.adventure;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public final class LiteBukkitAdventurePlatformFactory {

    private LiteBukkitAdventurePlatformFactory() {
    }

    public static LiteCommandsBuilder<CommandSender> builder(Server server, String fallbackPrefix, AudienceProvider audienceProvider) {
        KyoriAudienceExtractor kyoriAudienceExtractor = new KyoriAudienceExtractor(audienceProvider);

        return LiteBukkitFactory.builder(server, fallbackPrefix)
                .argument(Component.class, new KyoriComponentArgument())
                .argument(Component.class, "color", new KyoriColoredComponentArgument())

                .contextualBind(Audience.class, new KyoriAudienceContextual(kyoriAudienceExtractor))

                .resultHandler(Component.class, new KyoriComponentHandler(kyoriAudienceExtractor))
                .resultHandler(String.class, new StringHandler(kyoriAudienceExtractor));
    }

}
