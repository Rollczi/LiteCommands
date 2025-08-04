package dev.rollczi.example.bukkit.adventure.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;

@Command(name = "keys")
public class KeyCommand {

    @Execute(name = "adventure")
    String executeKey(@Arg Key key) {
        return "<pride> Your adventure key: " + key.asString();
    }

    @Execute(name = "bukkit")
    String executeBukkit(@Arg NamespacedKey key) {
        return  "<pride> Your bukkit key: " + key.toString();
    }

}
