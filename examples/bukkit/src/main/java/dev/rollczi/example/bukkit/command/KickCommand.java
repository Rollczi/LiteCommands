package dev.rollczi.example.bukkit.command;

import dev.rollczi.example.bukkit.util.ChatUtil;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.command.Command;
import org.bukkit.Server;
import org.bukkit.entity.Player;

@Command(name = "kick")
@Permission("dev.rollczi.kick")
public class KickCommand {

    @Execute
    public void kickPlayer(Server server, @Arg("target") Player target, @Join("reason") String reason) {
        target.kickPlayer(ChatUtil.color(reason));
        server.broadcastMessage(ChatUtil.color("&cPlayer &7" + target.getName() + " &chas been kicked!"));
    }

}
