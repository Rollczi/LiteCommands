package dev.rollczi.litecommands.adventure.bukkit.platform;

import dev.rollczi.litecommands.adventure.LiteAdventureExtension;
import net.kyori.adventure.platform.AudienceProvider;
import org.bukkit.command.CommandSender;

public class LiteBukkitAdventurePlatformExtension extends LiteAdventureExtension<CommandSender> {

    public LiteBukkitAdventurePlatformExtension(AudienceProvider audienceProvider) {
        super(new BukkitAdventurePlatformAudienceProvider(audienceProvider));
    }

}
