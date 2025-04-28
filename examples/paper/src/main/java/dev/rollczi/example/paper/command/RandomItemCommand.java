package dev.rollczi.example.paper.command;

import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.varargs.Varargs;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Command(name = "randomitem")
public class RandomItemCommand {

    @Execute
    public void randomItem(@Context Player player, @Varargs(delimiter = " | ") List<Material> materials) {
        if (materials.isEmpty()) {
            player.sendMessage("You must specify at least one material!");
            return;
        }

        Material material = materials.get((int) (Math.random() * materials.size()));
        player.getInventory().addItem(new ItemStack(material));
    }

}
