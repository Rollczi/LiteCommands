package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.platform.AbstractSimplePlatform;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import org.bukkit.command.CommandSender;

class BukkitPlatform extends AbstractSimplePlatform<CommandSender, LiteBukkitSettings> implements Platform<CommandSender, LiteBukkitSettings> {

    BukkitPlatform(LiteBukkitSettings settings) {
        super(settings, sender -> new BukkitPlatformSender(sender));
    }

    @Override
    protected void hook(CommandRoute<CommandSender> commandRoute, PlatformInvocationListener<CommandSender> invocationHook, PlatformSuggestionListener<CommandSender> suggestionHook) {
        BukkitCommand bukkitSimpleCommand = new BukkitCommand(settings, commandRoute, invocationHook, suggestionHook);

        this.settings.tabCompleter().register(settings.fallbackPrefix(), bukkitSimpleCommand);
        this.settings.commandsRegistry().register(commandRoute.getName(), settings.fallbackPrefix(), bukkitSimpleCommand);
    }

    @Override
    protected void unhook(CommandRoute<CommandSender> commandRoute) {
        for (String name : commandRoute.names()) {
            this.settings.commandsRegistry().unregister(name, settings.fallbackPrefix());
            this.settings.tabCompleter().unregister(name);
        }
    }

}
