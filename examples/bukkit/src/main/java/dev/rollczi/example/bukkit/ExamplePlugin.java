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
import dev.rollczi.litecommands.annotations.LiteAnnotationsExtension;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.LiteBukkitSettings;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.bukkit.tools.BukkitPlayerArgument;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.permission.MissingPermissions;
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
        LiteBukkitSettings settings = new LiteBukkitSettings()
            .fallbackPrefix("test")
            .nativePermissions(false);

        this.liteCommands = LiteBukkitFactory.builder(this.getServer(), settings)
            // Arguments
            .argument(Location.class, new LocationArgument())
            .argument(World.class, new WorldArgument(this.getServer()))
            .argument(GameMode.class, new GameModeArgument())
            .argument(Player.class, new BukkitPlayerArgument<>(this.getServer(), text -> "&cNie ma takiego gracza!"))

            // Contextual Bind
            .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>("&cKomenda tylko dla gracza!"))

            // Annotated commands extension
            .extension(new LiteAnnotationsExtension.Builder()
                .command(TeleportCommand.class, KickCommand.class, ConvertCommand.class)
                .build()
            )

            // Handlers
            .resultHandler(MissingPermissions.class, new MissingPermissionsHandlerImpl())
            .resultHandler(InvalidUsage.class, new InvalidUsageHandlerImpl())

            .register();
    }

    @Override
    public void onDisable() {
        this.liteCommands.unregister();
    }

}
