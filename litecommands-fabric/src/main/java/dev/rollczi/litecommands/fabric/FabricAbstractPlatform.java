package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSettings;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

abstract class FabricAbstractPlatform<SOURCE> extends AbstractPlatform<SOURCE, PlatformSettings> implements Platform<SOURCE, PlatformSettings> {

    protected final Map<UUID, FabricAbstractCommand<SOURCE>> fabricCommands = new HashMap<>();

    protected static boolean COMMAND_API_V2 = true;

    static {
        try {
            Class.forName("net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback");
        } catch (ClassNotFoundException e) {
            COMMAND_API_V2 = false;
        }
    }

    FabricAbstractPlatform(PlatformSettings settings) {
        super(settings);
        registerEvents();
    }

    protected abstract void registerEvents();

    @Override
    protected void hook(CommandRoute<SOURCE> commandRoute, PlatformInvocationListener<SOURCE> invocationHook, PlatformSuggestionListener<SOURCE> suggestionHook) {
        fabricCommands.put(commandRoute.getUniqueId(), createCommand(commandRoute, invocationHook, suggestionHook));
    }

    @Override
    protected void unhook(CommandRoute<SOURCE> commandRoute) {
        fabricCommands.remove(commandRoute.getUniqueId());
        // TODO: unregister command from dispatcher
    }

    protected abstract FabricAbstractCommand<SOURCE> createCommand(CommandRoute<SOURCE> commandRoute, PlatformInvocationListener<SOURCE> invocationHook, PlatformSuggestionListener<SOURCE> suggestionHook);
}
