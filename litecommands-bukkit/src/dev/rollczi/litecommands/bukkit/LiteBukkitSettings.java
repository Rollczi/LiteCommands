package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.platform.PlatformSettings;

public class LiteBukkitSettings implements PlatformSettings {

    private String fallbackPrefix;
    private boolean nativePermission = false;
    private BukkitCommandsProvider commandsProvider;

    public LiteBukkitSettings fallbackPrefix(String fallbackPrefix) {
        this.fallbackPrefix = fallbackPrefix;
        return this;
    }

    public LiteBukkitSettings nativePermissions(boolean nativePermission) {
        this.nativePermission = nativePermission;
        return this;
    }

    public LiteBukkitSettings commandsProvider(BukkitCommandsProvider commandsProvider) {
        this.commandsProvider = commandsProvider;
        return this;
    }

    String fallbackPrefix() {
        return fallbackPrefix;
    }

    boolean nativePermission() {
        return this.nativePermission;
    }

    BukkitCommandsProvider commandsProvider() {
        return this.commandsProvider;
    }

}
