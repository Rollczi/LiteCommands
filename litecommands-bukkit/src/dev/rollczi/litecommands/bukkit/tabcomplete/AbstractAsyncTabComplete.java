package dev.rollczi.litecommands.bukkit.tabcomplete;

import dev.rollczi.litecommands.bukkit.BukkitCommand;
import dev.rollczi.litecommands.command.CommandRoute;

abstract class AbstractAsyncTabComplete extends TabComplete {

    @Override
    public void register(String fallbackPrefix, BukkitCommand listener, CommandRoute<?> commandRoute) {
        super.register(fallbackPrefix, listener, commandRoute);
        listener.disableBasicSuggestions();
    }

    @Override
    public void unregister(String commandName) {
        BukkitCommand command = listeners.get(commandName);

        if (command != null) {
            command.enableBasicSuggestions();
        }

        super.unregister(commandName);
    }

}
