package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.command.Command;

@Command(name = "kick")
@Permission("dev.rollczi.kick")
public class KickCommand {

    /*@Execute TODO Add @Name and @Joiner
    public void kickPlayer(Server server, @Arg @Name("target") Player target, @Joiner String reason) {
        target.kickPlayer(ChatUtil.color(reason));
        server.broadcastMessage(ChatUtil.color("&cPlayer &7" + target.getName() + " &chas been kicked!"));
    }*/

}
