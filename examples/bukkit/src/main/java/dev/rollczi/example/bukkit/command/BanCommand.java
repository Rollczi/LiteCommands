package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.Command;
import dev.rollczi.litecommands.command.executor.Execute;
import dev.rollczi.litecommands.permission.Permission;
import org.bukkit.entity.Player;

@Command(name = "ban")
@Permission("example.ban")
public class BanCommand {

    @Execute
    public void ban(@Arg Player player) {
        player.kickPlayer("You have been banned!");
    }
}
