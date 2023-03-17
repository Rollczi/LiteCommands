package dev.rollczi.example.bukkit.command;

import dev.rollczi.example.bukkit.util.ChatUtil;
import dev.rollczi.litecommands.modern.annotation.argument.Arg;
import dev.rollczi.litecommands.modern.annotation.execute.Execute;
import dev.rollczi.litecommands.modern.annotation.permission.Permission;
import dev.rollczi.litecommands.modern.annotation.route.Route;
import org.bukkit.Server;
import org.bukkit.entity.Player;

@Route(name = "kick")
@Permission("dev.rollczi.kick")
public class KickCommand {

    /*@Execute TODO Add @Name and @Joiner
    public void kickPlayer(Server server, @Arg @Name("target") Player target, @Joiner String reason) {
        target.kickPlayer(ChatUtil.color(reason));
        server.broadcastMessage(ChatUtil.color("&cPlayer &7" + target.getName() + " &chas been kicked!"));
    }*/

}
