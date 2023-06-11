package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.platform.PlatformSettings;
import org.bukkit.Server;

public class LiteBukkitSettings implements PlatformSettings {

    private String fallbackPrefix;
    private boolean nativePermission = false;
    private BukkitCommandsRegistry commandsRegistry;

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

    public LiteBukkitSettings commandsRegistry(BukkitCommandsRegistry commandsRegistry) {
        this.commandsRegistry = commandsRegistry;
        return this;
    }

    String fallbackPrefix() {
        return fallbackPrefix;
    }

    boolean nativePermission() {
        return this.nativePermission;
    }

    BukkitCommandsRegistry commandsRegistry() {
        return this.commandsRegistry;
    }

}
