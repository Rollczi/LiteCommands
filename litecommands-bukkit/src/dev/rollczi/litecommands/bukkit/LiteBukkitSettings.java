package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.platform.PlatformSettings;
import java.util.regex.Pattern;
import org.bukkit.Server;

public class LiteBukkitSettings implements PlatformSettings {

    private String fallbackPrefix = "";
    private boolean nativePermission = false;
    private boolean syncSuggestionWarning = true;
    private Pattern playerNamePattern = Pattern.compile("^[a-zA-Z0-9_]{3,16}$");
    private boolean allowParseUnknownPlayers = false;
    private BukkitCommandsRegistry commandsRegistry;
    private TabComplete tabCompleter;

    public LiteBukkitSettings(BukkitCommandsRegistry commandsRegistry) {
        this.commandsRegistry = commandsRegistry;
    }

    public LiteBukkitSettings(Server server) {
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

    /**
     * Sets the pattern used to validate offline player names.
     * The default pattern is "^[a-zA-Z0-9_]{3,16}$".
     */
    public LiteBukkitSettings playerNamePattern(Pattern playerNamePattern) {
        this.playerNamePattern = playerNamePattern;
        return this;
    }

    /**
     * Sets whether to parse offline players that have played before.
     * Default is false, meaning only players that have played before will be parsed.
     * If set to true, it will allow parsing of unknown players, which may not have played before.
     */
    public LiteBukkitSettings allowParseUnknownPlayers(boolean allowParseUnknownPlayers) {
        this.allowParseUnknownPlayers = allowParseUnknownPlayers;
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

    String getFallbackPrefix() {
        return fallbackPrefix;
    }

    boolean isNativePermissionEnabled() {
        return this.nativePermission;
    }

    boolean isSyncSuggestionWarning() {
        return this.syncSuggestionWarning;
    }

    Pattern getPlayerNamePattern() {
        return playerNamePattern;
    }

    boolean isParseUnknownPlayersAllowed() {
        return allowParseUnknownPlayers;
    }

    BukkitCommandsRegistry getCommandsRegistry() {
        return this.commandsRegistry;
    }

    TabComplete getTabCompleter() {
        return this.tabCompleter;
    }

}
