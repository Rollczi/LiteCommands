package dev.rollczi.example.bukkit.user;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.command.CommandSender;

@Command(name = "user")
public class UserCommand {

    @Execute(name = "show")
    void show(@Context CommandSender sender, @Arg User user) {
        sender.sendMessage("User: " + user.getUuid() + " " + user.getName());
    }

}
