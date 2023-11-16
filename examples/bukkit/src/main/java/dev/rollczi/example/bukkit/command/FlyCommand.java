package dev.rollczi.example.bukkit.command;

import dev.rollczi.example.bukkit.validator.IsNotOp;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "fly", aliases = {"f"})
@Permission("example.fly")
public class FlyCommand {

    @Execute
    void execute(@Context Player sender) {
        sender.setAllowFlight(!sender.getAllowFlight());
        sender.sendMessage("You can " + (sender.getAllowFlight() ? "now" : "no longer") + " fly!");
    }

    @Execute
    void executeOther(@Context CommandSender sender, @Arg @IsNotOp Player target) {
        target.setAllowFlight(!target.getAllowFlight());
        target.sendMessage("You can " + (target.getAllowFlight() ? "now" : "no longer") + " fly!");
        sender.sendMessage("You " + (target.getAllowFlight() ? "enabled" : "disabled") + " fly for " + target.getName());
    }

}
