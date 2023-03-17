package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.modern.annotation.argument.Arg;
import dev.rollczi.litecommands.modern.annotation.execute.Execute;
import dev.rollczi.litecommands.modern.annotation.permission.Permission;
import dev.rollczi.litecommands.modern.annotation.route.Route;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@Route(name = "gamemode", aliases = "gm")
@Permission("example.gamemode")
public class GameModeCommand {

    @Execute
    public void execute(Player player, @Arg GameMode gameMode) {
        player.setGameMode(gameMode);
        player.sendMessage("Your gamemode has been changed to " + gameMode.name());
    }

}
