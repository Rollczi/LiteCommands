package dev.rollczi.example.bukkit;

import dev.rollczi.example.bukkit.argument.GameModeArgument;
import dev.rollczi.example.bukkit.command.GiveCommand;
import dev.rollczi.example.bukkit.command.KickAllCommand;
import dev.rollczi.example.bukkit.command.MuteCommand;
import dev.rollczi.example.bukkit.command.ConvertCommand;
import dev.rollczi.example.bukkit.command.FlyCommand;
import dev.rollczi.example.bukkit.command.GameModeCommand;
import dev.rollczi.example.bukkit.command.KickCommand;
import dev.rollczi.example.bukkit.command.NumberCommand;
import dev.rollczi.example.bukkit.command.OfflineInfoCommand;
import dev.rollczi.example.bukkit.command.RandomItemCommand;
import dev.rollczi.example.bukkit.command.TeleportCommand;
import dev.rollczi.example.bukkit.command.currency.CurrencyBalanceCommand;
import dev.rollczi.example.bukkit.command.currency.CurrencyCommand;
import dev.rollczi.example.bukkit.command.currency.CurrencyService;
import dev.rollczi.example.bukkit.validator.IsNotOpValidator;
import dev.rollczi.example.bukkit.validator.IsNotOp;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.example.bukkit.handler.ExampleInvalidUsageHandler;
import dev.rollczi.example.bukkit.handler.ExampleMissingPermissionsHandler;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.join.JoinArgument;
import dev.rollczi.litecommands.programmatic.LiteCommand;
import dev.rollczi.litecommands.strict.StrictMode;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {

    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {
        // [!] you don't have to use this service, it's just for demonstration [!]
        CurrencyService currencyService = new CurrencyService();

        this.liteCommands = LiteCommandsBukkit.builder()
            // configure bukkit platform
            .settings(settings -> settings
                .fallbackPrefix("my-plugin") // fallback prefix - used by bukkit to identify command
                .nativePermissions(false) // enable/disable bukkit permissions system
            )

            // Commands
            .commands(
                new OfflineInfoCommand(),
                new ConvertCommand(),
                new GameModeCommand(),
                new KickCommand(),
                new KickAllCommand(),
                new MuteCommand(),
                new TeleportCommand(),
                new FlyCommand(),
                new GiveCommand(),
                new RandomItemCommand(),
                new NumberCommand(),
                new CurrencyCommand(currencyService),
                new CurrencyBalanceCommand(currencyService)
            )

            // Custom annotation validators
            .annotations(configuration -> configuration
                .validator(Player.class, IsNotOp.class, new IsNotOpValidator())
            )

            // Programmatic commands
            .commands(
                new LiteCommand<CommandSender>("ban")
                    .permissions("example.ban")
                    .argument("player", Player.class)
                    .execute(context -> {
                        Player player = context.argument("player", Player.class);
                        player.kickPlayer("You have been banned!");
                    })
            )

            // change default messages
            .message(LiteBukkitMessages.LOCATION_INVALID_FORMAT, input -> "&cInvalid location format: &7" + input)

            // Arguments @Arg
            .argument(GameMode.class, new GameModeArgument())

            // Suggestions, if you want you can override default argument suggesters
            .argumentSuggestion(Integer.class, SuggestionResult.of("1", "2", "3"))
            .argumentSuggestion(String.class, JoinArgument.KEY, SuggestionResult.of("Simple suggestion", "Simple suggestion 2"))

            .message(LiteBukkitMessages.PLAYER_ONLY, "&cOnly player can execute this command!")
            .message(LiteBukkitMessages.PLAYER_NOT_FOUND, input -> "&cPlayer &7" + input + " &cnot found!")

            // Handlers for missing permissions and invalid usage
            .missingPermission(new ExampleMissingPermissionsHandler())
            .invalidUsage(new ExampleInvalidUsageHandler())

            // Schematic generator is used to generate schematic for command, for example when you run invalid command.
            .schematicGenerator(SchematicFormat.angleBrackets())

            // strict mode - you can enable/disable strict mode for each command (default is enabled)
            // if strict mode is enabled, the command will fail if the user provides too many arguments
            .strictMode(StrictMode.ENABLED)

            .build();
    }

    @Override
    public void onDisable() {
        // unregister all commands from bukkit
        if (this.liteCommands != null) {
            this.liteCommands.unregister();
        }    
    }

}
