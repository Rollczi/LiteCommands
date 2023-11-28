package dev.rollczi.example.bukkit.command;

import dev.rollczi.example.bukkit.util.ChatUtil;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.bind.Bind;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.quoted.Quoted;
import org.bukkit.Server;
import org.bukkit.entity.Player;

@Command(name = "mute")
@Permission("dev.rollczi.mute")
public class MuteCommand {

    @Execute
    void banPlayer(@Bind Server server, @Arg Player target, @Arg @Quoted String reason, @Flag("-s") boolean silent) {
        if (!silent) {
            server.broadcastMessage(ChatUtil.color("&cPlayer &7" + target.getName() + " &chas been muted! Reason: &7" + reason));
        }
    }

}
