package dev.rollczi.example.bukkit.adventure.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.command.Command;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@Command(name = "gamemode", aliases = "gm")
@Permission("example.gamemode")
public class GameModeCommand {

    @Execute
    public void execute(@Context Player player, @Arg GameMode gameMode) {
        player.setGameMode(gameMode);
        player.sendMessage("Your gamemode has been changed to " + gameMode.name());
    }

}
