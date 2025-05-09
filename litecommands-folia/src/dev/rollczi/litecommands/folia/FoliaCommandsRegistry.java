package dev.rollczi.litecommands.folia;

import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;

public interface FoliaCommandsRegistry {

    boolean register(@NotNull String label, @NotNull String fallbackPrefix, @NotNull Command command);

    void unregister(@NotNull String label, String fallbackPrefix);

}
