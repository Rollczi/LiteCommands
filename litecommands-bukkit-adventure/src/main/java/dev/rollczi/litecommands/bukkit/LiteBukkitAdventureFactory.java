package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.implementation.LiteFactory;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public final class LiteBukkitAdventureFactory {

    private LiteBukkitAdventureFactory() {
    }

    public static LiteCommandsBuilder<CommandSender> builder(Server server, String fallbackPrefix, AudienceProvider audienceProvider) {
        KyoriComponentHandler componentHandler = new KyoriComponentHandler(audienceProvider);

        return LiteBukkitFactory.builder(server, fallbackPrefix)
                .resultHandler(Component.class, componentHandler)
                .resultHandler(String.class, new StringHandler(componentHandler));
    }

}
