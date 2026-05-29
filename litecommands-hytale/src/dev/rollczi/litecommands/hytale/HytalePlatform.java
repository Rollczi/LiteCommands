package dev.rollczi.litecommands.hytale;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.hytale.stubs.HytaleSource;
import dev.rollczi.litecommands.permission.PermissionService;
import dev.rollczi.litecommands.platform.AbstractSimplePlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;

class HytalePlatform extends AbstractSimplePlatform<HytaleSource, LiteHytaleSettings> {

    // FIXME: API-DROP
    private final CommandRegistry commandRegistry;
    private final PermissionService permissionService;

    // FIXME: API-DROP
    public HytalePlatform(CommandRegistry commandRegistry, LiteHytaleSettings settings, PermissionService permissionService) {
        super(settings, source -> new HytaleSender(source));
        this.commandRegistry = commandRegistry;
        this.permissionService = permissionService;
    }

    @Override
    protected void hook(CommandRoute<HytaleSource> commandRoute, PlatformInvocationListener<HytaleSource> invocationHook, PlatformSuggestionListener<HytaleSource> suggestionHook) {
        HytaleCommand hytaleCommand = new HytaleCommand(settings, commandRoute, permissionService, invocationHook, suggestionHook);
        // FIXME: API-DROP
        this.commandRegistry.registerCommand(hytaleCommand);
    }

    @Override
    protected void unhook(CommandRoute<HytaleSource> commandRoute) {
        for (String name : commandRoute.names()) {
            // FIXME: API-DROP
            this.commandRegistry.unregisterCommand(name);
        }
    }

}
