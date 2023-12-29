package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Command(name = "kickall")
@Permission("dev.rollczi.kickall")
public class KickAllCommand {

    @Execute(name = "-all")
    public void kickAll() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.kickPlayer("Server is restarting!");
        }
    }

    @Execute
    void kickAll(@Arg Player[] players) {
        for (Player player : players) {
            player.kickPlayer("Server is restarting!");
        }
    }

}
