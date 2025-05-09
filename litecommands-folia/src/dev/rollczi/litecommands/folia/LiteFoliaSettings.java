package dev.rollczi.litecommands.folia;

import dev.rollczi.litecommands.platform.PlatformSettings;
import org.bukkit.Server;

public class LiteFoliaSettings implements PlatformSettings {

    private String fallbackPrefix = "";
    private boolean nativePermission = false;
    private boolean syncSuggestionWarning = true;
    private FoliaCommandsRegistry commandsRegistry;
    private TabComplete tabCompleter;

    public LiteFoliaSettings(FoliaCommandsRegistry commandsRegistry) {
        this.commandsRegistry = commandsRegistry;
    }

    public LiteFoliaSettings(Server server) {
        this.commandsRegistry = FoliaCommandsRegistryImpl.create(server);
    }

    public LiteFoliaSettings fallbackPrefix(String fallbackPrefix) {
        this.fallbackPrefix = fallbackPrefix;
        return this;
    }

    public LiteFoliaSettings nativePermissions(boolean nativePermission) {
        this.nativePermission = nativePermission;
        return this;
    }

    public LiteFoliaSettings syncSuggestionWarning(boolean syncSuggestionWarning) {
        this.syncSuggestionWarning = syncSuggestionWarning;
        return this;
    }

    public LiteFoliaSettings commandsRegistry(FoliaCommandsRegistry commandsRegistry) {
        this.commandsRegistry = commandsRegistry;
        return this;
    }

    public LiteFoliaSettings tabCompleter(TabComplete tabCompleter) {
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

    FoliaCommandsRegistry commandsRegistry() {
        return this.commandsRegistry;
    }

    TabComplete tabCompleter() {
        return this.tabCompleter;
    }

}
