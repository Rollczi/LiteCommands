package dev.rollczi.example.bukkit.command;

import dev.rollczi.example.bukkit.util.ChatUtil;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.argument.Name;
import dev.rollczi.litecommands.argument.joiner.Joiner;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import org.bukkit.Server;
import org.bukkit.entity.Player;

@Route(name = "kick")
@Permission("dev.rollczi.kick")
public class KickCommand {

    @Execute(min = 2)
    public void kickPlayer(Server server, @Arg @Name("target") Player target, @Joiner String reason) {
        target.kickPlayer(ChatUtil.color(reason));
        server.broadcastMessage(ChatUtil.color("&cPlayer &7" + target.getName() + " &chas been kicked!"));
    }

}
