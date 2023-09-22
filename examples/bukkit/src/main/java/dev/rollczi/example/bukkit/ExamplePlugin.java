package dev.rollczi.example.bukkit;

import dev.rollczi.example.bukkit.argument.GameModeArgument;
import dev.rollczi.litecommands.LiteCommand;
import dev.rollczi.litecommands.LiteCommandsProgrammatic;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.example.bukkit.command.ConvertCommand;
import dev.rollczi.example.bukkit.command.KickCommand;
import dev.rollczi.example.bukkit.command.TeleportCommand;
import dev.rollczi.example.bukkit.handler.InvalidUsageHandlerImpl;
import dev.rollczi.example.bukkit.handler.MissingPermissionsHandlerImpl;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.adventure.LiteAdventureExtension;
import dev.rollczi.litecommands.annotations.LiteAnnotationCommands;
import dev.rollczi.litecommands.join.JoinArgument;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.bukkit.tools.BukkitPlayerArgument;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        this.liteCommands = LiteCommandsBukkit.builder()
            // configure bukkit platform
            .settings(settings -> settings
                .fallbackPrefix("my-plugin") // fallback prefix - used by bukkit to identify command
                .nativePermissions(false) // enable/disable bukkit permissions system
            )

            // Commands
            .commands(LiteAnnotationCommands.of(
                new TeleportCommand(),
                new KickCommand(),
                new ConvertCommand()
            ))
            .commands(LiteCommandsProgrammatic.of(
                new LiteCommand<CommandSender>("ban")
                    .permissions("example.ban")
                    .argument("player", Player.class)
                    .onExecute(context -> {
                        Player player = context.argument("player", Player.class);
                        player.kickPlayer("You have been banned!");
                    })
            ))

            // change default messages
            .message(LiteBukkitMessages.LOCATION_INVALID_FORMAT, input -> "&cInvalid location format: &7" + input)

            // Arguments @Arg
            .argument(GameMode.class, new GameModeArgument())
            .argument(Player.class, new BukkitPlayerArgument<>(this.getServer(), text -> "&cPlayer not found!"))

            // Suggestions, if you want you can override default argument suggesters
            .argumentSuggester(String.class, SuggestionResult.of("name", "argument"))
            .argumentSuggester(Integer.class, SuggestionResult.of("1", "2", "3"))
            .argumentSuggester(String.class, JoinArgument.KEY, SuggestionResult.of("Simple suggestion", "Simple suggestion 2"))

            // Context resolver for @Context Player
            .context(Player.class, new BukkitOnlyPlayerContextual<>("&cOnly player can execute this command!"))

            // Handlers for missing permissions and invalid usage
            .missingPermission(new MissingPermissionsHandlerImpl())
            .invalidUsage(new InvalidUsageHandlerImpl())

            // Schematic generator is used to generate schematic for command, for example when you run invalid command.
            .schematicGenerator(SchematicFormat.angleBrackets())

            // register additional Kyori Adventure features for rgb, hex, gradient, click, hover, etc.
            // more: https://docs.advntr.dev/minimessage/format.html
            .extension(new LiteAdventureExtension<>(), extension -> extension
                .miniMessage(true) // enable mini message format (<red>, <gradient:red:blue>, <#ff0000>, etc.)
                .legacyColor(true) // enable legacy color format (&c, &a, etc.)
            )

            .build();
    }

    @Override
    public void onDisable() {
        // unregister all commands from bukkit
        this.liteCommands.unregister();
    }

}
