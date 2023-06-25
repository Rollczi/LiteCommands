package dev.rollczi.litecommands.paper;

import dev.rollczi.litecommands.adventure.LiteAdventureExtension;
import org.bukkit.command.CommandSender;

public final class LitePaperExtension extends LiteAdventureExtension<CommandSender> {

    public LitePaperExtension() {
        super(new AdventureAudiencePaperProvider());
    }

}
