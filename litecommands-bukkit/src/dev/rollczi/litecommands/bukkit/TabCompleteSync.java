package dev.rollczi.litecommands.bukkit;

class TabCompleteSync extends TabComplete {

    @Override
    void register(String fallbackPrefix, BukkitCommand listener) {
        super.register(fallbackPrefix, listener);
        listener.setSyncTabComplete(true);
    }

    @Override
    void unregister(String commandName) {
        BukkitCommand command = listeners.get(commandName);

        if (command != null) {
            command.setSyncTabComplete(false);
        }

        super.unregister(commandName);
    }

}
