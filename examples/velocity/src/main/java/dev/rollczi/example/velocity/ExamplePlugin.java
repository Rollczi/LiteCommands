package dev.rollczi.example.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.rollczi.example.velocity.argument.PlayerArgument;
import dev.rollczi.example.velocity.argument.RegisteredServerArgument;
import dev.rollczi.example.velocity.command.MsgCommand;
import dev.rollczi.example.velocity.command.SendCommand;
import dev.rollczi.example.velocity.handler.InvalidUsageHandlerImpl;
import dev.rollczi.example.velocity.handler.PermissionMessage;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.argument.profile.ProfileNamespaces;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import dev.rollczi.litecommands.schematic.SchematicFormat;
import dev.rollczi.litecommands.velocity.LiteVelocityFactory;
import dev.rollczi.litecommands.velocity.tools.VelocityOnlyPlayerContextual;

@Plugin(id = "example-plugin", name = "ExamplePlugin", version = "3.10.9", authors = "Rollczi")
public class ExamplePlugin {

    private final ProxyServer proxyServer;

    private LiteCommands<CommandSource> liteCommands;

    @Inject
    public ExamplePlugin(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.liteCommands = LiteVelocityFactory.builder(this.proxyServer)
            // configure velocity settings
            .settings(settings -> settings
                .nativePermissions(false) // enable/disable velocity native permissions
            )

            // Commands
            .commands(
                new SendCommand(),
                new MsgCommand()
            )

            // Arguments @Arg
            .argument(Player.class, new PlayerArgument(this.proxyServer))
            .argument(RegisteredServer.class, new RegisteredServerArgument(this.proxyServer))

            // Suggestions, if you want you can override default argument suggesters
            .argumentSuggestion(String.class, SuggestionResult.of("name", "argument"))
            .argumentSuggestion(Integer.class, SuggestionResult.of("1", "2", "3"))
            .argumentSuggestion(String.class, ProfileNamespaces.JOIN, SuggestionResult.of("Simple suggestion", "Simple suggestion 2"))

            // Context resolvers @Context
            .context(Player.class, new VelocityOnlyPlayerContextual<>("&cOnly player can execute this command!"))

            // Handlers for missing permissions and invalid usage
            .missingPermission(new PermissionMessage())
            .invalidUsage(new InvalidUsageHandlerImpl())

            // Schematic generator is used to generate schematic for command, for example when you run invalid command.
            .schematicGenerator(SchematicFormat.angleBrackets())

            .build();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        this.liteCommands.unregister();
    }

}
