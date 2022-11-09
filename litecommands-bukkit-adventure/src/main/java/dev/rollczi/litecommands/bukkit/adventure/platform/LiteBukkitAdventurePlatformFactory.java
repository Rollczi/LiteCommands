package dev.rollczi.litecommands.bukkit.adventure.platform;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.adventure.LiteBukkitAdventureFactory;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public final class LiteBukkitAdventurePlatformFactory {

    private LiteBukkitAdventurePlatformFactory() {
    }

    public static LiteCommandsBuilder<CommandSender> builder(Server server, String fallbackPrefix, AudienceProvider audienceProvider) {
        return builder(server, fallbackPrefix, audienceProvider, false);
    }

    public static LiteCommandsBuilder<CommandSender> builder(Server server, String fallbackPrefix, AudienceProvider audienceProvider, boolean supportsMiniMessage) {
        KyoriAudienceAdventurePlatformProvider audienceAdventurePlatformProvider = new KyoriAudienceAdventurePlatformProvider(audienceProvider);

        return LiteBukkitAdventureFactory.builder(server, fallbackPrefix, audienceAdventurePlatformProvider, supportsMiniMessage);
    }

    public static LiteCommandsBuilder<CommandSender> builder(Server server, String fallbackPrefix, AudienceProvider audienceProvider, ComponentSerializer<Component, ?, String> kyoriComponentSerializer) {
        KyoriAudienceAdventurePlatformProvider audienceAdventurePlatformProvider = new KyoriAudienceAdventurePlatformProvider(audienceProvider);
        return LiteBukkitAdventureFactory.builder(server, fallbackPrefix, audienceAdventurePlatformProvider, kyoriComponentSerializer);
    }

}
