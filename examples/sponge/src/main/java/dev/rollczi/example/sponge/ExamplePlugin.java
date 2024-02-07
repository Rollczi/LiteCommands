package dev.rollczi.example.sponge;

import com.google.inject.Inject;
import dev.rollczi.example.sponge.command.KickCommand;
import dev.rollczi.example.sponge.command.TeleportCommand;
import dev.rollczi.example.sponge.handler.InvalidUsageHandlerImpl;
import dev.rollczi.example.sponge.handler.PermissionMessage;
import dev.rollczi.litecommands.join.JoinArgument;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import dev.rollczi.litecommands.sponge.LiteSpongeFactory;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

@Plugin("litecommands-example")
public class ExamplePlugin {

    @Inject
    public ExamplePlugin(Logger logger, PluginContainer pluginContainer, Game game) {
        logger.info("Initializing example sponge platform...");

        LiteSpongeFactory.builder(pluginContainer, game)

            // Commands
            .commands(
                new TeleportCommand(),
                new KickCommand()
            )

            // Suggestions, if you want you can override default argument suggesters
            .argumentSuggestion(int.class, SuggestionResult.of("1", "2", "3"))
            .argumentSuggestion(Component.class, JoinArgument.KEY, SuggestionResult.of("Simple suggestion", "Simple suggestion 2"))

            // Handlers for missing permissions and invalid usage
            .missingPermission(new PermissionMessage())
            .invalidUsage(new InvalidUsageHandlerImpl())

            // Schematic generator is used to generate schematic for command, for example, when you run invalid command.
            .schematicGenerator(SchematicFormat.angleBrackets())

            .build();

        logger.info("Initialized example sponge platform");
    }
}
