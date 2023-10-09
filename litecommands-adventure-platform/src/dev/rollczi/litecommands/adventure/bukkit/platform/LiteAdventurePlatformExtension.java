package dev.rollczi.litecommands.adventure.bukkit.platform;

import dev.rollczi.litecommands.adventure.LiteAdventureExtension;
import net.kyori.adventure.platform.AudienceProvider;

public class LiteAdventurePlatformExtension<SENDER> extends LiteAdventureExtension<SENDER> {

    public LiteAdventurePlatformExtension(AudienceProvider audienceProvider) {
        super(new AdventurePlatformAudienceProvider<>(audienceProvider));
    }

}
