package dev.rollczi.litecommands.paper;

import dev.rollczi.litecommands.adventure.AdventureAudienceProvider;
import dev.rollczi.litecommands.shared.Preconditions;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;

class AdventureAudiencePaperProvider implements AdventureAudienceProvider<CommandSender> {

    @Override
    public Audience sender(CommandSender commandSender) {
        Preconditions.notNull(commandSender, "commandSender");

        return commandSender;
    }

}
