package dev.rollczi.litecommands.bukkit.adventure;

import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;

public interface KyoriAudienceProvider {

    Audience sender(CommandSender commandSender);

}
