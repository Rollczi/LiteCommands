package dev.rollczi.example.bukkit.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.bukkit.Location;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;

@Command(name = "cat")
public class CatCommand {

    @Execute
    void executeCat(@Context Location currentLocation, @Arg Cat.Type type) {
        Cat cat = (Cat) currentLocation.getWorld().spawnEntity(currentLocation, EntityType.CAT);
        cat.setCatType(type);
    }

}
