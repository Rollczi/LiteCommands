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
import dev.rollczi.example.velocity.command.SendCommand;
import dev.rollczi.example.velocity.handler.InvalidUsage;
import dev.rollczi.example.velocity.handler.PermissionMessage;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.velocity.LiteVelocityFactory;

@Plugin(id = "example-plugin", name = "ExamplePlugin", version = "1.0.0", authors = { "Rollczi" }, url = "https://rollczi.dev/")
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
            // Arguments
            .argument(Player.class, new PlayerArgument(this.proxyServer))
            .argument(RegisteredServer.class, new RegisteredServerArgument(this.proxyServer))

            // Commands
            .commandInstance(new SendCommand())

            // Handlers
            .permissionHandler(new PermissionMessage())
            .invalidUsageHandler(new InvalidUsage())

            .register();
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        this.liteCommands.getPlatform().unregisterAll();
    }

}
