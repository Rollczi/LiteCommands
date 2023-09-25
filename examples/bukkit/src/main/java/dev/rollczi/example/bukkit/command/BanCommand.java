package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.entity.Player;

@Command(name = "ban")
@Permission("example.ban")
public class BanCommand {

    @Execute
    public void ban(@Arg Player player) {
        player.kickPlayer("You have been banned!");
    }
}
