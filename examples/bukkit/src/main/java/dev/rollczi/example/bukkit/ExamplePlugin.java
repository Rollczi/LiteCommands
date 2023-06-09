package dev.rollczi.example.bukkit;

import dev.rollczi.example.bukkit.argument.GameModeArgument;
import dev.rollczi.example.bukkit.argument.LocationArgument;
import dev.rollczi.example.bukkit.argument.WorldArgument;
import dev.rollczi.example.bukkit.command.ConvertCommand;
import dev.rollczi.example.bukkit.command.KickCommand;
import dev.rollczi.example.bukkit.command.TeleportCommand;
import dev.rollczi.example.bukkit.handler.InvalidUsageHandlerImpl;
import dev.rollczi.example.bukkit.handler.MissingPermissionsHandlerImpl;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.adventure.LiteAdventureExtension;
import dev.rollczi.litecommands.annotations.LiteAnnotationCommnads;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.bukkit.tools.BukkitPlayerArgument;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        this.liteCommands = LiteCommandsBukkit.builder(this.getServer())
            .platformSettings(settings -> settings
                .fallbackPrefix("test")
                .nativePermissions(false)
            )

            .commands(LiteAnnotationCommnads.of(
                new TeleportCommand(),
                new KickCommand(),
                new ConvertCommand()
            ))

            .argument(Location.class, new LocationArgument())
            .argument(World.class, new WorldArgument(this.getServer()))
            .argument(GameMode.class, new GameModeArgument())
            .argument(Player.class, new BukkitPlayerArgument<>(this.getServer(), text -> "&cNie ma takiego gracza!"))

            .context(Player.class, new BukkitOnlyPlayerContextual<>("&cKomenda tylko dla gracza!"))

            .missingPermission(new MissingPermissionsHandlerImpl())
            .invalidUsage(new InvalidUsageHandlerImpl())

            .extension(new LiteAdventureExtension<>(), extension -> extension
                .miniMessage(true)
                .legacyColor(true)
            )

            .build();
    }

    @Override
    public void onDisable() {
        this.liteCommands.unregister();
    }

}
