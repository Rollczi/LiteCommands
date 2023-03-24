package dev.rollczi.litecommands.paper;

import dev.rollczi.litecommands.adventure.LiteAdventureExtension;
import dev.rollczi.litecommands.platform.LiteSettings;
import org.bukkit.command.CommandSender;

public final class LitePaperExtension<C extends LiteSettings> extends LiteAdventureExtension<CommandSender, C> {

    public LitePaperExtension() {
        super(new AdventureAudiencePaperProvider());
    }

}
