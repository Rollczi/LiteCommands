package dev.rollczi.example.bukkit.command;

import dev.rollczi.example.bukkit.util.ChatUtil;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.bind.Bind;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.quoted.Quoted;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.time.Duration;

@Command(name = "kick")
@Permission("dev.rollczi.kick")
public class BanCommand {

    @Execute
    void banPlayer(@Bind Server server, @Arg Player target, @Arg @Quoted String reason, @Flag("-s") boolean silent) {
        target.ban(ChatUtil.color(reason), Duration.ofSeconds(5), "Admin");

        if (!silent) {
            server.broadcastMessage(ChatUtil.color("&cPlayer &7" + target.getName() + " &chas been banned!"));
        }
    }

}
