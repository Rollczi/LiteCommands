package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.bukkit.tabcomplete.TabComplete;
import dev.rollczi.litecommands.platform.PlatformSettings;
import org.bukkit.Server;

public class LiteBukkitSettings implements PlatformSettings {

    private String fallbackPrefix = "";
    private boolean nativePermission = false;
    private boolean syncSuggestionWarning = true;
    private BukkitCommandsRegistry commandsRegistry;
    private TabComplete tabCompleter;

    public LiteBukkitSettings() {
    }

    LiteBukkitSettings(Server server) {
        this.commandsRegistry = BukkitCommandsRegistryImpl.create(server);
    }

    public LiteBukkitSettings fallbackPrefix(String fallbackPrefix) {
        this.fallbackPrefix = fallbackPrefix;
        return this;
    }

    public LiteBukkitSettings nativePermissions(boolean nativePermission) {
        this.nativePermission = nativePermission;
        return this;
    }

    public LiteBukkitSettings syncSuggestionWarning(boolean syncSuggestionWarning) {
        this.syncSuggestionWarning = syncSuggestionWarning;
        return this;
    }

    public LiteBukkitSettings commandsRegistry(BukkitCommandsRegistry commandsRegistry) {
        this.commandsRegistry = commandsRegistry;
        return this;
    }

    public LiteBukkitSettings tabCompleter(TabComplete tabCompleter) {
        this.tabCompleter = tabCompleter;
        return this;
    }

    String fallbackPrefix() {
        return fallbackPrefix;
    }

    boolean nativePermission() {
        return this.nativePermission;
    }

    boolean syncSuggestionWarning() {
        return this.syncSuggestionWarning;
    }

    BukkitCommandsRegistry commandsRegistry() {
        return this.commandsRegistry;
    }

    TabComplete tabCompleter() {
        return this.tabCompleter;
    }

}
