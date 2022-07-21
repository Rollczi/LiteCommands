package dev.rollczi.litecommands.bukkit.adventure.paper;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.adventure.LiteBukkitAdventureFactory;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

public final class LitePaperAdventureFactory {

    private LitePaperAdventureFactory() {
    }

    public static LiteCommandsBuilder<CommandSender> builder(Server server, String fallbackPrefix, boolean supportsMiniMessage) {
        KyoriAudiencePaperProvider audienceAdventurePlatformProvider = new KyoriAudiencePaperProvider();

        return LiteBukkitAdventureFactory.builder(server, fallbackPrefix, audienceAdventurePlatformProvider, supportsMiniMessage);
    }

}
