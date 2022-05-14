package dev.rollczi.example.bukkit;

import dev.rollczi.example.bukkit.argument.WorldArgument;
import dev.rollczi.example.bukkit.handler.InvalidUsage;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.bukkit.tools.BukkitPlayerArgument;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.example.bukkit.argument.LocationArgument;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
public class ExamplePlugin extends JavaPlugin {

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        this.liteCommands = LiteBukkitFactory.builder(this.getServer(), "example-plugin")
                // Arguments
                .argumentMultilevel(Location.class, new LocationArgument())
                .argument(World.class, new WorldArgument(this.getServer()))
                .argument(Player.class, new BukkitPlayerArgument(this.getServer(), "&cNie ma takiego gracza!"))

                // Contextual Bind
                .contextualBind(Player.class, new BukkitOnlyPlayerContextual("&cKomenda tylko dla gracza!"))

                // Commands
                .command(TeleportCommand.class)

                // Handlers
                .invalidUsageHandler(new InvalidUsage())

                .register();
    }

}
