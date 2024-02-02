package dev.rollczi.example.sponge;

import com.google.inject.Inject;
import dev.rollczi.example.sponge.command.TeleportCommand;
import dev.rollczi.example.sponge.handler.InvalidUsageHandlerImpl;
import dev.rollczi.example.sponge.handler.PermissionMessage;
import dev.rollczi.litecommands.annotations.LiteCommandsAnnotations;
import dev.rollczi.litecommands.join.JoinArgument;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import dev.rollczi.litecommands.sponge.LiteSpongeFactory;
import dev.rollczi.litecommands.sponge.argument.ServerPlayerArgument;
import dev.rollczi.litecommands.sponge.contextual.SpongeOnlyPlayerContextual;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.plugin.PluginContainer;

public class ExamplePlugin {

    @Inject
    public ExamplePlugin(Logger logger, PluginContainer pluginContainer, Game game) {
        logger.info("Initializing example sponge platform...");

        LiteSpongeFactory.builder(pluginContainer)

            // Commands
            .commands(LiteCommandsAnnotations.of(
                new TeleportCommand()
            ))

            // Arguments @Arg
            .argument(ServerPlayer.class, new ServerPlayerArgument<>(
                game,
                "Server isn't available on this environment",
                "No such player found"
            ))

            // Suggestions, if you want you can override default argument suggesters
            .argumentSuggester(String.class, SuggestionResult.of("name", "argument"))
            .argumentSuggester(Integer.class, SuggestionResult.of("1", "2", "3"))
            .argumentSuggester(String.class, JoinArgument.KEY, SuggestionResult.of("Simple suggestion", "Simple suggestion 2"))

            // Context resolvers @Context
            .context(Player.class, new SpongeOnlyPlayerContextual<>("&cOnly player can execute this command!"))

            // Handlers for missing permissions and invalid usage
            .missingPermission(new PermissionMessage())
            .invalidUsage(new InvalidUsageHandlerImpl())

            // Schematic generator is used to generate schematic for command, for example, when you run invalid command.
            .schematicGenerator(SchematicFormat.angleBrackets())

            .build();

        logger.info("Initialized example sponge platform");
    }
}
