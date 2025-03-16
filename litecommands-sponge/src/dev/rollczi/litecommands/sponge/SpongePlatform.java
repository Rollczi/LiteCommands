package dev.rollczi.litecommands.sponge;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.permission.PermissionService;
import dev.rollczi.litecommands.platform.AbstractSimplePlatform;
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

public class SpongePlatform extends AbstractSimplePlatform<CommandCause, LiteSpongeSettings> {

    private final PluginContainer plugin;
    private final PermissionService permissionService;
    private final Map<UUID, SpongeCommand> commands = new HashMap<>();

    public SpongePlatform(PluginContainer plugin, LiteSpongeSettings settings, PermissionService permissionService) {
        super(settings, commandCause -> new SpongeSender(commandCause));
        this.plugin = plugin;
        this.permissionService = permissionService;
        Sponge.eventManager().registerListeners(plugin, this);
    }

    @Override
    protected void hook(CommandRoute<CommandCause> commandRoute, PlatformInvocationListener<CommandCause> invocationHook, PlatformSuggestionListener<CommandCause> suggestionHook) {
        commands.put(commandRoute.getUniqueId(), new SpongeCommand(permissionService, commandRoute, invocationHook, suggestionHook));
    }

    @Override
    protected void unhook(CommandRoute<CommandCause> commandRoute) {
        // Sponge doesn't provide a way to unregister commands (TODO: check if it's possible by reflections)
    }

    @Listener
    public void onRegisterRawCommands(RegisterCommandEvent<Command.Raw> event) {
        for (SpongeCommand command : commands.values()) {
            CommandRoute<CommandCause> commandRoute = command.getCommandRoute();
            event.register(plugin, command, commandRoute.getName(), commandRoute.getAliases().toArray(new String[0]));
        }
    }
}
