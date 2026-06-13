package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.argument.CaseInsensitive;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.context.Sender;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.Location;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

@Command(name = "cat")
public class CatCommand {

    @Execute
    void executeCat(@Context Location currentLocation, @Arg Cat.Type type) {
        Cat cat = (Cat) currentLocation.getWorld().spawnEntity(currentLocation, EntityType.CAT);
        cat.setCatType(type);
    }

    @Execute
    void executeCat2(@Sender Player player, @Context Location currentLocation, @Arg TestEnum type) {
        Cat cat = (Cat) currentLocation.getWorld().spawnEntity(currentLocation, EntityType.CAT);
        player.sendMessage("" + type);
    }

    enum TestEnum {
        ONE, TWO, THREE, FOUR;
    }
}
