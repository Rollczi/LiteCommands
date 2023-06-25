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
import dev.rollczi.litecommands.annotations.LiteAnnotationCommands;
import dev.rollczi.litecommands.annotations.argument.join.Join;
import dev.rollczi.litecommands.argument.suggestion.SuggestionResult;
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
        this.liteCommands = LiteCommandsBukkit.builder()
            // configure litecommands
            .settings(settings -> settings
                .fallbackPrefix("my-plugin")
            )

            // register commands
            .commands(LiteAnnotationCommands.of(
                new TeleportCommand(),
                new KickCommand(),
                new ConvertCommand()
            ))

            // register custom argument
            .argument(Location.class, new LocationArgument())
            .argument(World.class, new WorldArgument(this.getServer()))
            .argument(GameMode.class, new GameModeArgument())
            .argument(Player.class, new BukkitPlayerArgument<>(this.getServer(), text -> "&cPlayer not found!"))

            // override default argument suggester
            .argumentSuggester(String.class, SuggestionResult.of("name", "argument"))
            .argumentSuggester(Integer.class, SuggestionResult.of("1", "2", "3"))
            .argumentSuggester(String.class, Join.ARGUMENT_KEY, SuggestionResult.of("Simple suggestion", "Simple suggestion 2"))

            // register context
            .context(Player.class, new BukkitOnlyPlayerContextual<>("&cOnly player can execute this command!"))

            // register handlers for missing permissions and invalid usage
            .missingPermission(new MissingPermissionsHandlerImpl())
            .invalidUsage(new InvalidUsageHandlerImpl())

            // register additional Adventure features
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
