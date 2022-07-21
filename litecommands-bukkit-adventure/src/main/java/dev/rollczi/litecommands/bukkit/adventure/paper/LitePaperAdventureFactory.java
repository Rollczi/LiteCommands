package dev.rollczi.litecommands.bukkit.adventure.paper;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.adventure.LiteBukkitAdventureFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public final class LitePaperAdventureFactory {

    private LitePaperAdventureFactory() {
    }

    public static LiteCommandsBuilder<CommandSender> builder(Server server, String fallbackPrefix) {
        return builder(server, fallbackPrefix, false);
    }

    public static LiteCommandsBuilder<CommandSender> builder(Server server, String fallbackPrefix, boolean supportsMiniMessage) {
        KyoriAudiencePaperProvider audienceAdventurePlatformProvider = new KyoriAudiencePaperProvider();

        return LiteBukkitAdventureFactory.builder(server, fallbackPrefix, audienceAdventurePlatformProvider, supportsMiniMessage);
    }

    public static LiteCommandsBuilder<CommandSender> builder(Server server, String fallbackPrefix, ComponentSerializer<Component, ?, String> kyoriComponentSerializer) {
        KyoriAudiencePaperProvider audienceAdventurePlatformProvider = new KyoriAudiencePaperProvider();

        return LiteBukkitAdventureFactory.builder(server, fallbackPrefix, audienceAdventurePlatformProvider, kyoriComponentSerializer);
    }

}
