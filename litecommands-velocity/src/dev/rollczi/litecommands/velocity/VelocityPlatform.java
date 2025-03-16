package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.permission.PermissionService;
import dev.rollczi.litecommands.platform.AbstractSimplePlatform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;

class VelocityPlatform extends AbstractSimplePlatform<CommandSource, LiteVelocitySettings> {

    private final CommandManager commandManager;
    private final PermissionService permissionService;

    public VelocityPlatform(CommandManager commandManager, LiteVelocitySettings settings, PermissionService permissionService) {
        super(settings, source -> new VelocitySender(source));
        this.commandManager = commandManager;
        this.permissionService = permissionService;
    }

    @Override
    protected void hook(CommandRoute<CommandSource> commandRoute, PlatformInvocationListener<CommandSource> invocationHook, PlatformSuggestionListener<CommandSource> suggestionHook) {
        VelocityCommand velocityCommand = new VelocityCommand(settings, commandRoute, permissionService, invocationHook, suggestionHook);

        CommandMeta meta = commandManager.metaBuilder(commandRoute.getName())
            .aliases(commandRoute.getAliases().toArray(new String[0]))
            .build();

        this.commandManager.register(meta, velocityCommand);
    }

    @Override
    protected void unhook(CommandRoute<CommandSource> commandRoute) {
        for (String name : commandRoute.names()) {
            this.commandManager.unregister(name);
        }
    }

}
