package dev.rollczi.litecommands.bukkit.adventure;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public final class LiteBukkitAdventureFactory {

    public static LiteCommandsBuilder<CommandSender> builder(Server server, String fallbackPrefix, KyoriAudienceProvider kyoriAudienceProvider) {
        return LiteBukkitFactory.builder(server, fallbackPrefix)
                .argument(Component.class, new KyoriComponentArgument())
                .argument(Component.class, "color", new KyoriColoredComponentArgument())

                .contextualBind(Audience.class, new KyoriAudienceContextual(kyoriAudienceProvider))

                .resultHandler(Component.class, new KyoriComponentHandler(kyoriAudienceProvider))
                .resultHandler(String.class, new StringHandler(kyoriAudienceProvider));
    }

}
