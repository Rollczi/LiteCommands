package dev.rollczi.litecommands.folia;

class TabCompleteSync extends TabComplete {

    @Override
    void register(String fallbackPrefix, FoliaCommand listener) {
        super.register(fallbackPrefix, listener);
        listener.setSyncTabComplete(true);
    }

    @Override
    void unregister(String commandName) {
        FoliaCommand command = listeners.get(commandName);

        if (command != null) {
            command.setSyncTabComplete(false);
        }

        super.unregister(commandName);
    }

}
