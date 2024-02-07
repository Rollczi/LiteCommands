package dev.rollczi.litecommands.sponge;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;

public class LiteSpongePlatform extends AbstractPlatform<CommandCause, LiteSpongeSettings> {

    private final PluginContainer plugin;
    private final Map<UUID, LiteSpongeCommand> commands = new HashMap<>();

    public LiteSpongePlatform(PluginContainer plugin, LiteSpongeSettings settings) {
        super(settings);
        this.plugin = plugin;
        Sponge.eventManager().registerListeners(plugin, this);
    }

    @Override
    protected void hook(CommandRoute<CommandCause> commandRoute, PlatformInvocationListener<CommandCause> invocationHook, PlatformSuggestionListener<CommandCause> suggestionHook) {
        commands.put(commandRoute.getUniqueId(), new LiteSpongeCommand(commandRoute, invocationHook, suggestionHook));
    }

    @Override
    protected void unhook(CommandRoute<CommandCause> commandRoute) {
        // Sponge doesn't provide a way to unregister commands (TODO: check if it's possible by reflections)
    }

    @Listener
    public void onRegisterRawCommands(RegisterCommandEvent<Command.Raw> event) {
        for (LiteSpongeCommand command : commands.values()) {
            CommandRoute<CommandCause> commandRoute = command.getCommandRoute();
            event.register(plugin, command, commandRoute.getName(), commandRoute.getAliases().toArray(new String[0]));
        }
    }
}
