package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.platform.PlatformSettings;

public class LiteBungeeSettings implements PlatformSettings {

    private boolean nativePermissions = false;

    public LiteBungeeSettings nativePermissions(boolean nativePermissions) {
        this.nativePermissions = nativePermissions;
        return this;
    }

    boolean isNativePermissions() {
        return this.nativePermissions;
    }

}
