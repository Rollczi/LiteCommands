package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.platform.AbstractPlatform;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import org.bukkit.command.CommandSender;

class BukkitPlatform extends AbstractPlatform<CommandSender, LiteBukkitSettings> implements Platform<CommandSender, LiteBukkitSettings> {

    BukkitPlatform(LiteBukkitSettings settings) {
        super(settings);
    }

    @Override
    protected void hook(CommandRoute<CommandSender> commandRoute, PlatformInvocationListener<CommandSender> invocationHook, PlatformSuggestionListener<CommandSender> suggestionHook) {
        BukkitCommand bukkitSimpleCommand = new BukkitCommand(settings, commandRoute, invocationHook, suggestionHook);

        this.settings.commandsRegistry().register(commandRoute.getName(), settings.fallbackPrefix(), bukkitSimpleCommand);
        this.settings.tabCompleter().register(settings.fallbackPrefix(), bukkitSimpleCommand);
    }

    @Override
    protected void unhook(CommandRoute<CommandSender> commandRoute) {
        for (String name : commandRoute.names()) {
            this.settings.commandsRegistry().unregister(name, settings.fallbackPrefix());
            this.settings.tabCompleter().unregister(name);
        }
    }

}
