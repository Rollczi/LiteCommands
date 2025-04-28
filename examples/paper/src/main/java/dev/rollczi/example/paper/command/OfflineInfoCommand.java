package dev.rollczi.example.paper.command;

import dev.rollczi.example.paper.util.ChatUtil;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import java.time.Duration;
import java.time.Instant;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@Command(name = "offline-info")
public class OfflineInfoCommand {

    @Execute
    void executeOfflineInfo(
        @Context CommandSender sender,
        @Arg OfflinePlayer offlinePlayer
    ) {
        String online = "&7(" + (offlinePlayer.isOnline() ? "&aonline" : "&coffline") + "&7)";
        Instant lastPlayed = Instant.ofEpochMilli(offlinePlayer.getLastPlayed());
        String lastPlayedFormatted = Duration.between(lastPlayed, Instant.now()).toHours() + "h";

        sender.sendMessage(ChatUtil.color("&7Player: &e" + offlinePlayer.getName() + " " + online));
        sender.sendMessage(ChatUtil.color("&7uuid: " + offlinePlayer.getUniqueId()));
        sender.sendMessage(ChatUtil.color("&7Last played: &e" + lastPlayedFormatted));
    }

}
