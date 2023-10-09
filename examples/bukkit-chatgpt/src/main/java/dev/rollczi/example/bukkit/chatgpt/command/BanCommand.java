package dev.rollczi.example.bukkit.chatgpt.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.chatgpt.annotation.ChatGpt;
import org.bukkit.entity.Player;

@Command(name = "ban")
public class BanCommand {

    @Execute
    void execute(@Arg Player player, @Join @ChatGpt(topic = "Reason for ban") String reason) {
        player.kickPlayer(reason);
    }

}
