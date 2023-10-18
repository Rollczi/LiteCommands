package dev.rollczi.litecommands.sponge.internal;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.sponge.LiteSpongeSettings;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;

import java.util.ArrayList;
import java.util.Collection;

@ApiStatus.Internal
public class LiteSpongePlatform extends AbstractPlatform<CommandCause, LiteSpongeSettings> {

    private final PluginContainer plugin;

    private final Collection<LiteSpongeCommand> pendingUnregisteredCommands = new ArrayList<>();

    public LiteSpongePlatform(PluginContainer plugin, @NotNull LiteSpongeSettings settings) {
        super(settings);
        this.plugin = plugin;
        Sponge.eventManager().registerListeners(plugin, this);
    }

    @Override
    protected void hook(CommandRoute<CommandCause> commandRoute, PlatformInvocationListener<CommandCause> invocationHook, PlatformSuggestionListener<CommandCause> suggestionHook) {
        pendingUnregisteredCommands.add(new LiteSpongeCommand(commandRoute, invocationHook, suggestionHook));
    }

    @Override
    protected void unhook(CommandRoute<CommandCause> commandRoute) {
        // unsupported by sponge
    }

    @Listener
    public void onRegisterCommand(RegisterCommandEvent<LiteSpongeCommand> event) {
        for (LiteSpongeCommand command : pendingUnregisteredCommands) {
            event.register()
        }
    }
}
