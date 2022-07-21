package dev.rollczi.litecommands.bukkit.adventure.platform;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.adventure.LiteBukkitAdventureFactory;
import net.kyori.adventure.platform.AudienceProvider;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public final class LiteBukkitAdventurePlatformFactory {

    private LiteBukkitAdventurePlatformFactory() {
    }

    public static LiteCommandsBuilder<CommandSender> builder(Server server, String fallbackPrefix, AudienceProvider audienceProvider, boolean supportsMiniMessage) {
        KyoriAudienceAdventurePlatformProvider audienceAdventurePlatformProvider = new KyoriAudienceAdventurePlatformProvider(audienceProvider);

        return LiteBukkitAdventureFactory.builder(server, fallbackPrefix, audienceAdventurePlatformProvider, supportsMiniMessage);
    }

}
