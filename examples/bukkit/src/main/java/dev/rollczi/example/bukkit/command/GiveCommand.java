package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Command(name = "give")
public class GiveCommand {

    @SuppressWarnings("DataFlowIssue")
    @Execute
    public void execute(
        @Context CommandSender commandSender,
        @Arg("nick") Player target,
        @Arg("item") Material item,
        @OptionalArg("amount") Integer amount
    ) {
        if (amount != null) {
            amount = 1;
        }

        commandSender.sendMessage("You gave " + target.getName() + " " + amount + "x " + item.name());
        target.getInventory().addItem(new ItemStack(item, amount));
    }

}
