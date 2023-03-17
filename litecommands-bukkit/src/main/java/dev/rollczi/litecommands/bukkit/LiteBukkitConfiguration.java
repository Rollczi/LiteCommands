package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.modern.LiteConfiguration;

public class LiteBukkitConfiguration implements LiteConfiguration {

    private String fallbackPrefix;
    private boolean nativePermission = false;

    public LiteBukkitConfiguration fallbackPrefix(String fallbackPrefix) {
        this.fallbackPrefix = fallbackPrefix;
        return this;
    }

    public LiteBukkitConfiguration nativePermissions(boolean nativePermission) {
        this.nativePermission = nativePermission;
        return this;
    }

    String getFallbackPrefix() {
        return fallbackPrefix;
    }

    boolean isNativePermission() {
        return this.nativePermission;
    }

}
