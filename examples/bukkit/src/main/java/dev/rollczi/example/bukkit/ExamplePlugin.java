package dev.rollczi.example.bukkit;

import dev.rollczi.example.bukkit.argument.GameModeArgument;
import dev.rollczi.example.bukkit.command.BanCommand;
import dev.rollczi.example.bukkit.command.ChatGptCommand;
import dev.rollczi.litecommands.chatgpt.ChatGptModel;
import dev.rollczi.litecommands.chatgpt.LiteChatGptExtension;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.example.bukkit.handler.ExampleInvalidUsageHandler;
import dev.rollczi.example.bukkit.handler.ExampleMissingPermissionsHandler;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.annotations.LiteCommandsAnnotations;
import dev.rollczi.litecommands.join.JoinArgument;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.bukkit.LiteCommandsBukkit;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
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

            .extension(new LiteChatGptExtension<>(settings -> settings
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .model(ChatGptModel.GPT_4)
                .temperature(0.9)
                .tokensLimit(1, 64)
            ))

            // Commands
            .commands(LiteCommandsAnnotations.of(
                new ChatGptCommand(),
                new BanCommand()
            ))
    /*        .commands(LiteCommandsProgrammatic.of(
                new LiteCommand<CommandSender>("ban")
                    .permissions("example.ban")
                    .argument("player", Player.class)
                    .onExecute(context -> {
                        Player player = context.argument("player", Player.class);
                        player.kickPlayer("You have been banned!");
                    })
            ))*/

            // change default messages
            .message(LiteBukkitMessages.LOCATION_INVALID_FORMAT, input -> "&cInvalid location format: &7" + input)

            // Arguments @Arg
            .argument(GameMode.class, new GameModeArgument())

            // Suggestions, if you want you can override default argument suggesters
            .argumentSuggester(String.class, SuggestionResult.of("name", "argument"))
            .argumentSuggester(Integer.class, SuggestionResult.of("1", "2", "3"))
            .argumentSuggester(String.class, JoinArgument.KEY, SuggestionResult.of("Simple suggestion", "Simple suggestion 2"))

            .message(LiteBukkitMessages.PLAYER_ONLY, "&cOnly player can execute this command!")
            .message(LiteBukkitMessages.PLAYER_NOT_FOUND, input -> "&cPlayer &7" + input + " &cnot found!")

            // Handlers for missing permissions and invalid usage
            .missingPermission(new ExampleMissingPermissionsHandler())
            .invalidUsage(new ExampleInvalidUsageHandler())

            // Schematic generator is used to generate schematic for command, for example when you run invalid command.
            .schematicGenerator(SchematicFormat.angleBrackets())

            .build();
    }

    @Override
    public void onDisable() {
        // unregister all commands from bukkit
        this.liteCommands.unregister();
    }

}
