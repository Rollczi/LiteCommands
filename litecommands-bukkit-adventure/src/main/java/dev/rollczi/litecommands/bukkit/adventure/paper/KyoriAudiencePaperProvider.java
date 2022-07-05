package dev.rollczi.litecommands.bukkit.adventure.paper;

import dev.rollczi.litecommands.bukkit.adventure.KyoriAudienceProvider;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;

class KyoriAudiencePaperProvider implements KyoriAudienceProvider {

    @Override
    public Audience sender(CommandSender commandSender) {
        if (commandSender instanceof Audience) {
            return (Audience) commandSender;
        }

        throw new IllegalArgumentException("Unsupported command sender type: " + commandSender.getClass().getName());
    }

}
